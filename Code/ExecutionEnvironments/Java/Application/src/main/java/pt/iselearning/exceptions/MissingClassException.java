package pt.iselearning.exceptions;

/**
 * Exception class to represent cases when a public class name cannot be parsed from code text.
 */
public class MissingClassException extends ApplicationException {
    public MissingClassException(String type, String title, String message, String instance) {
        super(type, title, message, instance);
    }
}
