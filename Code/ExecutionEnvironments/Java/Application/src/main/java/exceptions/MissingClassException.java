package exceptions;

/**
 * Exception class to represent cases when a public class name cannot be parsed from code text.
 */
public class MissingClassException extends Exception {

    public MissingClassException(String message) {
        super(message);
    }
}
