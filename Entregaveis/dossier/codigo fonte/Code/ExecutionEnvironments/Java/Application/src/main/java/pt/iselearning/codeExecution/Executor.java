package pt.iselearning.codeExecution;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import pt.iselearning.exceptions.CommandExecutionTimeout;
import pt.iselearning.exceptions.MissingClassException;
import pt.iselearning.models.ExecutionResult;
import pt.iselearning.utils.CodeParser;
import pt.iselearning.utils.CommandExecutor;
import pt.iselearning.utils.JarLocations;
import pt.iselearning.utils.JavaFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * This class is responsible for orchestrating the execution of a java project with a single class and maybe a test
 * class if initialized for that purpose.
 */
public class Executor implements AutoCloseable {
    private final CommandExecutor cmdExec;
    private String codeClassName;
    private String code;

    private String testClassName;
    private String testCode;

    /**
     * Constants holding the path value needed for the execution
     */
    private final static String PACKAGE_NAME = "app";
    private final Path CODE_OUTPUT = Paths.get(".", "codeOutput", UUID.randomUUID().toString());

    public Executor(String code, String testCode) throws MissingClassException, IOException {
        this.code = String.format("package %s;", PACKAGE_NAME) + CodeParser.removeEndLinesAndDuplicateSpaces(code);
        this.codeClassName = CodeParser.extractClassName(this.code);
        if(this.codeClassName == null) {
            throw new MissingClassException(
                    "MissingPublicClass",
                    "No public class name on code",
                    "Cannot parse public class name from code.",
                    "/execute/java/code/compile/publicClass"
            );
        }
        if(testCode != null) {
            this.testCode = String.format("package %s; import %s.%s;",PACKAGE_NAME, PACKAGE_NAME, codeClassName) + CodeParser.removeEndLinesAndDuplicateSpaces(testCode);
            this.testClassName = CodeParser.extractClassName(this.testCode);
            if(this.testClassName == null) {
                throw new MissingClassException(
                        "MissingPublicClass",
                        "No public class name on unit tests",
                        "Cannot parse public class name from unit test code.",
                        "/execute/java/tests/compile/publicClass"
                );
            }
        }
        this.cmdExec = new CommandExecutor();
    }

    /**
     * Orchestrates code compilation for the java project.
     *
     * @throws IOException
     * @throws InterruptedException
     * @return
     */
    public ExecutionResult compileCode() throws IOException, InterruptedException {
        Path fullPathToCodeFile = CODE_OUTPUT.resolve(PACKAGE_NAME).resolve(String.format("%s.java", codeClassName));
        ExecutionResult codeCompileRes = compile(fullPathToCodeFile, code, new Path[]{CODE_OUTPUT});
        if (codeCompileRes.getWasError()) {
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
    public ExecutionResult executeUnitTests() throws IOException, InterruptedException, CommandExecutionTimeout {
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
    public ExecutionResult executeCode() throws IOException, InterruptedException, CommandExecutionTimeout {
        Path[] classpath = new Path[]{CODE_OUTPUT};
        return cmdExec.executionCommand(CommandExecutor.CodeType.CODE, classpath,
                String.format("%s.%s", PACKAGE_NAME, codeClassName));
    }

    @Override
    public void close() throws Exception {
        if(CODE_OUTPUT.toFile().exists()) {
            FileUtils.deleteDirectory(CODE_OUTPUT.toFile());
        }
        this.cmdExec.close();
    }
}
