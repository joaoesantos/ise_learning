package pt.iselearning.codeExecution;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import pt.iselearning.exceptions.MissingClassException;
import pt.iselearning.models.ExecutionResult;
import pt.iselearning.utils.CodeParser;
import pt.iselearning.utils.CommandExecutor;
import pt.iselearning.utils.JavaFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is responsible for orchestrating the execution of a java project with a single class and maybe a test
 * class if initialized for that purpose.
 */
public class Executor {
    private final CommandExecutor cmdExec;
    private String codeClassName;
    private String code;

    private String testClassName;
    private String testCode;

    /**
     * Constants holding the path value needed for the execution
     */
    private final static String PACKAGE_NAME = "app";
    private final static Path UNCOMPILED_OUTPUT = Paths.get(".", "out", "uncompiled");
    private final static Path COMPILED_OUTPUT = Paths.get(".", "out", "compiled");

    public Executor(String code, String testCode) throws MissingClassException, IOException {
        this.code = String.format("package %s;", PACKAGE_NAME) + CodeParser.removeEndLinesAndDuplicateSpaces(code);
        this.codeClassName = CodeParser.extractClassName(this.code);
        if(this.codeClassName == null) {
            throw new MissingClassException("Cannot parse public class name from code.");
        }
        if(testCode != null) {
            this.testCode = String.format("package %s;", PACKAGE_NAME) + CodeParser.removeEndLinesAndDuplicateSpaces(testCode);
            this.testClassName = CodeParser.extractClassName(this.testCode);
            if(this.testClassName == null) {
                throw new MissingClassException("Cannot parse public class name from unit test code.");
            }
        }
        this.cmdExec = CommandExecutor.getInstance();
    }

    /**
     * Orchestrates code compilation for the java project.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void compileCode() throws IOException, InterruptedException {
        FileUtils.deleteDirectory(UNCOMPILED_OUTPUT.toFile());
        FileUtils.deleteDirectory(COMPILED_OUTPUT.toFile());
        Path fullPathToCodeFile = UNCOMPILED_OUTPUT.resolve(String.format("%s.java", codeClassName));
        compile(fullPathToCodeFile, code, new Path[]{UNCOMPILED_OUTPUT});
        if(testCode != null) {
            Path junitJar = Paths.get(".", "libs", "junit-4.13.jar");
            Path fullPathToTestFile = UNCOMPILED_OUTPUT.resolve(String.format("%s.java", testClassName));
            compile(fullPathToTestFile, testCode, new Path[]{junitJar, UNCOMPILED_OUTPUT});
        }
    }

    /**
     * Orchestrates code compilation for a single java file, including file creation.
     *
     * @param fullPathToFile
     * @param code
     * @param classpath
     * @throws IOException
     * @throws InterruptedException
     */
    private void compile(Path fullPathToFile, String code, Path[] classpath) throws IOException, InterruptedException {
        JavaFile.createJavaFile(fullPathToFile, code);
        cmdExec.compileCommand(classpath, fullPathToFile, COMPILED_OUTPUT);
    }

    /**
     * Orchestrates execution of unit tests for the java project.
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ExecutionResult executeUnitTests() throws IOException, InterruptedException {
        Path junitJar = Paths.get(".", "libs", "junit-4.13.jar");
        Path hamcrestJar = Paths.get(".", "libs", "hamcrest-all-1.3.jar");
        Path[] classpath = new Path[]{junitJar, hamcrestJar, COMPILED_OUTPUT};
        String result = cmdExec.executionCommand(CommandExecutor.CodeType.TEST, classpath,
                String.format("%s.%s", PACKAGE_NAME, testClassName));
        return new ExecutionResult(result);
    }

    /**
     * Orchestrates execution of the main method for the java project.
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ExecutionResult executeCode() throws IOException, InterruptedException {
        Path[] classpath = new Path[]{COMPILED_OUTPUT};
        String result = cmdExec.executionCommand(CommandExecutor.CodeType.CODE, classpath,
                String.format("%s.%s", PACKAGE_NAME, codeClassName));
        return new ExecutionResult(result);
    }
}
