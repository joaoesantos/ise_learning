package pt.iselearning.exceptions;

/**
 * Generic Server exception to encapsulate problem json needed information.
 */
public class ApplicationException extends Exception {
    private String type;
    private String title;
    private String instance;

    public ApplicationException(String type, String title, String message, String instance) {
        super(message);
        this.type = type;
        this.title = title;
        this.instance = instance;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getInstance() {
        return instance;
    }
}
