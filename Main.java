import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * COS201 Mid-Semester Lab Assessment
 * Student Course Management System
 */
public class Main {

    private static final String DATA_FILE = resolveDataFile();
    private static final Scanner input = new Scanner(System.in);
    private static final CourseManager manager = new CourseManager();

    private static String resolveDataFile() {
        File currentDirFile = new File("courses.txt");
        if (currentDirFile.exists()) {
            return currentDirFile.getAbsolutePath();
        }
        File fallback = new File(System.getProperty("user.dir"), "courses.txt");
        return fallback.getAbsolutePath();
    }

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   STUDENT COURSE MANAGEMENT SYSTEM (COS 201)");
        System.out.println("==============================================");
        loadSavedCoursesAtStartup();
        runMenu();                       // starts the recursive menu
        input.close();
    }

    private static void loadSavedCoursesAtStartup() {
        try {
            int skipped = manager.loadFromFile(DATA_FILE);
            if (manager.size() > 0 || skipped == 0) {
                System.out.println("[OK] " + manager.size() + " saved course(s) loaded from " + DATA_FILE);
            } else {
                System.out.println("[!] No valid saved courses were found in " + DATA_FILE);
            }
        } catch (FileNotFoundException e) {
            System.out.println("[Info] No saved course file found. Starting with an empty list.");
        } catch (IOException e) {
            System.out.println("[Error] Could not read saved file: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // RECURSIVE MENU: displays itself again after every action until Exit
    // ------------------------------------------------------------------
    private static void runMenu() {
        printMenu();
        int choice = readMenuChoice();

        switch (choice) {
            case 1: addCourse();       break;
            case 2: viewCourses();     break;
            case 3: searchCourse();    break;
            case 4: computeTotal();    break;
            case 5: saveCourses();     break;
            case 6: loadCourses();     break;
            case 7:
                System.out.println("\nGoodbye! Remember to save your courses next time.");
                return;                  // base case: stops the recursion
            default:
                System.out.println("\n[!] Please choose a number from 1 to 7.");
        }
        runMenu();                       // recursive call: show the menu again
    }

    private static void printMenu() {
        System.out.println("\n------------- MAIN MENU -------------");
        System.out.println(" 1. Add Course");
        System.out.println(" 2. View All Courses");
        System.out.println(" 3. Search Course by Code");
        System.out.println(" 4. Compute Total Units");
        System.out.println(" 5. Save to File");
        System.out.println(" 6. Load from File");
        System.out.println(" 7. Exit Program");
        System.out.println("-------------------------------------");
    }

    /** Reads the menu choice; returns -1 for anything that is not a number. */
    private static int readMenuChoice() {
        System.out.print("Enter your choice (1-7): ");
        String line = input.hasNextLine() ? input.nextLine().trim() : "7";
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;                   // not a number: treated as an invalid choice
        }
    }

    // ------------------------------------------------------------------
    // Menu actions
    // ------------------------------------------------------------------
    private static void addCourse() {
        System.out.println("\n--- Add Course ---");
        System.out.print("Course code (e.g. COS201): ");
        String code = input.nextLine();
        System.out.print("Course title: ");
        String title = input.nextLine();
        System.out.print("Credit units (1-" + CourseManager.MAX_UNITS_PER_COURSE + "): ");
        String units = input.nextLine();

        try {
            manager.addCourse(code, title, units);
            System.out.println("\n[OK] Course added successfully.");
        } catch (InvalidCourseException e) {
            System.out.println("\n[Error] " + e.getMessage());
        }
    }

    private static void viewCourses() {
        System.out.println("\n--- All Courses ---");
        if (manager.isEmpty()) {
            System.out.println("No courses have been added yet.");
        } else {
            manager.displayAll();
        }
    }

    private static void searchCourse() {
        System.out.println("\n--- Search Course ---");
        System.out.print("Enter course code: ");
        String code = input.nextLine().trim();

        if (code.isEmpty()) {
            System.out.println("[Error] Course code cannot be empty.");
            return;
        }
        int index = manager.findIndexByCode(code, 0);   // recursive search
        if (index == -1) {
            System.out.println("[Not found] No course with code " + code.toUpperCase() + ".");
        } else {
            Course c = manager.getCourse(index);
            System.out.println("[Found]");
            System.out.println("  Code  : " + c.getCode());
            System.out.println("  Title : " + c.getTitle());
            System.out.println("  Units : " + c.getUnits());
        }
    }

    private static void computeTotal() {
        System.out.println("\n--- Total Units ---");
        int total = manager.totalUnits(0);              // recursive sum
        System.out.println("Number of courses : " + manager.size());
        System.out.println("Total credit units: " + total);
    }

    private static void saveCourses() {
        System.out.println("\n--- Save to File ---");
        if (manager.isEmpty()) {
            System.out.println("[!] Nothing to save. Add some courses first.");
            return;
        }
        try {
            manager.saveToFile(DATA_FILE);
            System.out.println("[OK] " + manager.size() + " course(s) saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("[Error] Could not save file: " + e.getMessage());
        }
    }

    private static void loadCourses() {
        System.out.println("\n--- Load from File ---");
        try {
            int skipped = manager.loadFromFile(DATA_FILE);
            System.out.println("[OK] " + manager.size() + " course(s) loaded from " + DATA_FILE);
            if (skipped > 0) {
                System.out.println("[!] " + skipped + " invalid line(s) were skipped.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("[Error] " + DATA_FILE + " was not found. Save a file first.");
        } catch (IOException e) {
            System.out.println("[Error] Could not read file: " + e.getMessage());
        }
    }
}
