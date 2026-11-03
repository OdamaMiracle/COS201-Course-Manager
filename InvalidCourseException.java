/**
 * Custom checked exception thrown whenever course data fails validation
 * (bad course code, empty title, invalid unit, duplicate code, etc.).
 */
public class InvalidCourseException extends Exception {

    public InvalidCourseException(String message) {
        super(message);
    }
}
