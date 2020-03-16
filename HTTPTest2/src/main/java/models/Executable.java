package models;

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

    public void setCode(String code) {
        this.code = code;
    }

    public void setUnitTests(String unitTests) {
        this.unitTests = unitTests;
    }

    public void setExecuteTests(boolean executeTests) {
        this.executeTests = executeTests;
    }

    public Executable(String code, String unitTests, boolean executeTests) {
        this.code = code;
        this.unitTests = unitTests;
        this.executeTests = executeTests;
    }
}