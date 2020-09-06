package pt.iselearning.models;

/**
 * Model responded by controller.
 */
public class ExecutionResult {
    private String rawResult;
    private boolean wasError;
    private long executionTime;

    public String getRawResult() {
        return rawResult;
    }

    public boolean getWasError() {
        return wasError;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public ExecutionResult(String rawResult, boolean wasError, long executionTime) {
        this.rawResult = rawResult;
        this.wasError = wasError;
        this.executionTime = executionTime;
    }
}
