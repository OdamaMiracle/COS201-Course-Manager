/**
 * Represents a single course taken by a student.
 * Holds the course code, title and credit units.
 */
public class Course {

    private final String code;
    private final String title;
    private final int units;

    public Course(String code, String title, int units) {
        this.code = code;
        this.title = title;
        this.units = units;
    }

    public String getCode()  { return code; }
    public String getTitle() { return title; }
    public int getUnits()    { return units; }

    /** Converts the course to one line of text for saving: CODE|TITLE|UNITS */
    public String toFileString() {
        return code + "|" + title + "|" + units;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-38s %5d", code, title, units);
    }
}
