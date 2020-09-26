package pt.iselearning.exceptions;

/**
 * Exception class to represent cases when a the command execution timesout.
 */
public class CommandExecutionTimeout extends ApplicationException {
    public CommandExecutionTimeout(String type, String title, String message, String instance) {
        super(type, title, message, instance);
    }
}
