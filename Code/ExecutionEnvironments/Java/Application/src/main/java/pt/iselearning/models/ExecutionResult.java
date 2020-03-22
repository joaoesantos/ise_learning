package pt.iselearning.models;

/**
 * Model responded by controller.
 */
public class ExecutionResult {
    private String rawResult;
    private boolean wasError;

    public String getRawResult() {
        return rawResult;
    }

    public boolean wasError() {
        return wasError;
    }

    public ExecutionResult(String rawResult, boolean wasError) {
        this.rawResult = rawResult;
        this.wasError = wasError;
    }
}
