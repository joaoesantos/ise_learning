package server;

public class Executable {
    private String code;
    private String unitTests;
    private boolean executeTests;

    public String getCode() {
        return code;
    }

    public String getUnitTests() {
        return unitTests;
    }

    public boolean getExecuteTests() {
        return executeTests;
    }

    enum ExecutionType {CODE, UNIT_TESTS};

    public Executable(String code, String unitTests, boolean executeTests) {
        this.code = code;
        this.unitTests = unitTests;
        this.executeTests = executeTests;
    }
}