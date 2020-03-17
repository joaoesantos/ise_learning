package models;

/**
 * Model responded by controller.
 */
public class ExecutionResult {
    private String rawResult;

    public String getRawResult() {
        return rawResult;
    }

    public ExecutionResult(String rawResult) {
        this.rawResult = rawResult;
    }
}
