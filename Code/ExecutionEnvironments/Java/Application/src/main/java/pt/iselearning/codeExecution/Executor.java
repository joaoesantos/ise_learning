package pt.iselearning.codeExecution;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import pt.iselearning.exceptions.MissingClassException;
import pt.iselearning.models.ExecutionResult;
import pt.iselearning.utils.CodeParser;
import pt.iselearning.utils.CommandExecutor;
import pt.iselearning.utils.JarLocations;
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
    private final static Path CODE_OUTPUT = Paths.get(".", "code");

    public Executor(String code, String testCode) throws MissingClassException, IOException {
        this.code = String.format("package %s;", PACKAGE_NAME) + CodeParser.removeEndLinesAndDuplicateSpaces(code);
        this.codeClassName = CodeParser.extractClassName(this.code);
        if(this.codeClassName == null) {
            throw new MissingClassException("Cannot parse public class name from code.");
        }
        if(testCode != null) {
            this.testCode = String.format("package %s; import %s.%s;",PACKAGE_NAME, PACKAGE_NAME, codeClassName) + CodeParser.removeEndLinesAndDuplicateSpaces(testCode);
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
     * @return
     */
    public ExecutionResult compileCode() throws IOException, InterruptedException {
        FileUtils.cleanDirectory(CODE_OUTPUT.toFile());
        Path fullPathToCodeFile = CODE_OUTPUT.resolve(PACKAGE_NAME).resolve(String.format("%s.java", codeClassName));
        ExecutionResult codeCompileRes = compile(fullPathToCodeFile, code, new Path[]{CODE_OUTPUT});
        if (codeCompileRes.wasError()) {
            return codeCompileRes;
        } else if (testCode != null) {
            return compileTestCode();
        }
        return codeCompileRes;
    }

    /**
     * Orchestrates test code compilation for the java project.
     *
     * @throws IOException
     * @throws InterruptedException
     * @return
     */
    private ExecutionResult compileTestCode() throws IOException, InterruptedException {
        Path fullPathToTestFile = CODE_OUTPUT.resolve(PACKAGE_NAME).resolve(String.format("%s.java", testClassName));
        return compile(fullPathToTestFile, testCode, new Path[]{JarLocations.JUNIT_JAR, CODE_OUTPUT});
    }

    /**
     * Orchestrates code compilation for a single java file, including file creation.
     *
     * @param fullPathToFile
     * @param code
     * @param classpath
     * @throws IOException
     * @throws InterruptedException
     * @return
     */
    private ExecutionResult compile(Path fullPathToFile, String code, Path[] classpath) throws IOException, InterruptedException {
        JavaFile.createJavaFile(fullPathToFile, code);
        return cmdExec.compileCommand(classpath, fullPathToFile, CODE_OUTPUT);
    }

    /**
     * Orchestrates execution of unit tests for the java project.
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ExecutionResult executeUnitTests() throws IOException, InterruptedException {
        Path[] classpath = new Path[]{JarLocations.JUNIT_JAR, JarLocations.HAMCREST_JAR, CODE_OUTPUT};
        return cmdExec.executionCommand(CommandExecutor.CodeType.TEST, classpath,
                String.format("%s.%s", PACKAGE_NAME, testClassName));
    }

    /**
     * Orchestrates execution of the main method for the java project.
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ExecutionResult executeCode() throws IOException, InterruptedException {
        Path[] classpath = new Path[]{CODE_OUTPUT};
        return cmdExec.executionCommand(CommandExecutor.CodeType.CODE, classpath,
                String.format("%s.%s", PACKAGE_NAME, codeClassName));
    }
}
