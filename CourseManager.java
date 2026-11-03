import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the list of courses and performs every operation on it:
 * adding (with validation), searching, totalling, saving and loading.
 */
public class CourseManager {

    public static final int MAX_UNITS_PER_COURSE = 6;

    private final List<Course> courses = new ArrayList<>();

    // ------------------------------------------------------------------
    // 1. ADD COURSE  (string processing + validation + exceptions)
    // ------------------------------------------------------------------
    public void addCourse(String rawCode, String rawTitle, String rawUnits)
            throws InvalidCourseException {

        // --- string processing: trim and normalise casing ---
        String code = rawCode == null ? "" : rawCode.trim().toUpperCase().replaceAll("\\s+", "");
        String title = rawTitle == null ? "" : normaliseTitle(rawTitle);

        // --- validation ---
        if (!code.matches("[A-Z]{3}\\d{3}")) {
            throw new InvalidCourseException(
                "Invalid course code '" + rawCode + "'. Use 3 letters followed by 3 digits (e.g. COS201).");
        }
        if (title.isEmpty()) {
            throw new InvalidCourseException("Course title cannot be empty.");
        }
        if (title.contains("|")) {
            throw new InvalidCourseException(
                    "Course title cannot contain the | character."
            );
        }
        int units;
        try {
            units = Integer.parseInt(rawUnits == null ? "" : rawUnits.trim());
        } catch (NumberFormatException e) {
            throw new InvalidCourseException("Units must be a whole number, but you entered '" + rawUnits + "'.");
        }
        if (units < 1 || units > MAX_UNITS_PER_COURSE) {
            throw new InvalidCourseException("Units must be between 1 and " + MAX_UNITS_PER_COURSE + ".");
        }
        if (findIndexByCode(code, 0) != -1) {
            throw new InvalidCourseException("Course " + code + " has already been added.");
        }

        courses.add(new Course(code, title, units));
    }

    /** Trims, collapses repeated spaces and applies Title Case (small words like "of" stay lower-case). */
    private String normaliseTitle(String raw) {
        String[] words = raw.trim().split("\\s+");
        String smallWords = " of and the to in for on a an ";
        StringBuilder sb = new StringBuilder();
        for (String w : words) {                       // iteration
            if (w.isEmpty()) continue;
            String lower = w.toLowerCase();
            if (sb.length() > 0) {
                sb.append(' ');
                if (smallWords.contains(" " + lower + " ")) {   // keep small words lower-case
                    sb.append(lower);
                    continue;
                }
            }
            sb.append(Character.toUpperCase(lower.charAt(0)));
            sb.append(lower.substring(1));
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // 2. VIEW ALL COURSES
    // ------------------------------------------------------------------
    public boolean isEmpty() { return courses.isEmpty(); }

    public void displayAll() {
        System.out.println(String.format("%-4s %-10s %-38s %5s", "S/N", "CODE", "TITLE", "UNITS"));
        System.out.println("-".repeat(61));
        for (int i = 0; i < courses.size(); i++) {      // iteration
            System.out.println(String.format("%-4d %s", i + 1, courses.get(i)));
        }
        System.out.println("-".repeat(61));
        System.out.println("Courses recorded: " + courses.size());
    }

    // ------------------------------------------------------------------
    // 3. SEARCH BY CODE  (RECURSIVE)
    // ------------------------------------------------------------------
    /**
     * Recursive linear search. Checks the course at 'index'; if it does not
     * match, the method calls itself with index + 1.
     * Base cases: end of list (-1 = not found) or a match (return index).
     */
    public int findIndexByCode(String code, int index) {
        String normalizedCode = code == null
                ? ""
                : code.trim().replaceAll("\\s+", "").toUpperCase();

        if (index >= courses.size()) {
            return -1;
        }

        if (courses.get(index).getCode().equals(normalizedCode)) {
            return index;
        }

        return findIndexByCode(normalizedCode, index + 1);
    }

    public Course getCourse(int index) { return courses.get(index); }

    // ------------------------------------------------------------------
    // 4. COMPUTE TOTAL UNITS  (RECURSIVE)
    // ------------------------------------------------------------------
    /**
     * Recursively sums the units: total(i) = units of course i + total(i + 1).
     */
    public int totalUnits(int index) {
        if (index >= courses.size()) return 0;                        // base case
        return courses.get(index).getUnits() + totalUnits(index + 1); // recursive call
    }

    public int size() { return courses.size(); }

    // ------------------------------------------------------------------
    // 5. SAVE TO FILE
    // ------------------------------------------------------------------
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Course c : courses) {
                writer.write(c.toFileString());
                writer.newLine();
            }
        }
    }

    // ------------------------------------------------------------------
    // 6. LOAD FROM FILE
    // ------------------------------------------------------------------
    /**
     * Loads courses from a file, replacing the current list. Malformed lines
     * are skipped and counted so one bad line does not stop the whole load.
     *
     * @return number of malformed lines that were skipped
     */
    public int loadFromFile(String filename) throws IOException {
        List<Course> backup = new ArrayList<>(courses);   // restore if reading fails
        courses.clear();
        int skipped = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {                // iteration
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                try {
                    if (parts.length != 3) throw new InvalidCourseException("Wrong number of fields");
                    addCourse(parts[0], parts[1], parts[2]);            // re-uses validation
                } catch (InvalidCourseException e) {
                    skipped++;
                }
            }
        } catch (IOException e) {
            courses.clear();
            courses.addAll(backup);
            throw e;
        }
        return skipped;
    }
}
