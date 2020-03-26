package pt.iselearning.codeExecution;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import pt.iselearning.exceptions.MissingClassException;
import pt.iselearning.models.ExecutionResult;
import pt.iselearning.utils.CodeParser;
import pt.iselearning.utils.CommandExecutor;
import pt.iselearning.utils.JarLocations;
import pt.iselearning.utils.KotlinFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is responsible for orchestrating the execution of a kotlin project with a single class and maybe a test
 * class if initialized for that purpose.
 */
public class Executor {
    private static final String CODE_FILE_NAME = "Code";
    private final CommandExecutor cmdExec;
    private String code;

    private String testClassName;
    private String testCode;

    /**
     * Constants holding the path value needed for the execution
     */
    private final static Path CODE_OUTPUT = Paths.get(".", "codeOutput");

    public Executor(String code, String testCode) throws MissingClassException, IOException {
        this.code = CodeParser.removeEndLinesAndDuplicateSpaces(code);
        if(testCode != null) {
            this.testCode = CodeParser.removeEndLinesAndDuplicateSpaces(testCode);
            this.testClassName = CodeParser.extractClassName(this.testCode);
            if(this.testClassName == null) {
                throw new MissingClassException("Cannot parse public class name from unit test code.");
            }
        }
        this.cmdExec = CommandExecutor.getInstance();
    }

    /**
     * Orchestrates code compilation for the koltin project.
     *
     * @throws IOException
     * @throws InterruptedException
     * @return
     */
    public ExecutionResult compileCode() throws IOException, InterruptedException {
        if(CODE_OUTPUT.toFile().exists()) {
            FileUtils.cleanDirectory(CODE_OUTPUT.toFile());
        }
        Path fullPathToCodeFile = CODE_OUTPUT.resolve(String.format("%s.kt", CODE_FILE_NAME));
        ExecutionResult codeCompileRes = compile(fullPathToCodeFile, code, new Path[]{}, CODE_FILE_NAME);
        if (codeCompileRes.wasError()) {
            return codeCompileRes;
        } else if (testCode != null) {
            return compileTestCode();
        }
        return codeCompileRes;
    }

    /**
     * Orchestrates test code compilation for the koltin project.
     *
     * @throws IOException
     * @throws InterruptedException
     * @return
     */
    private ExecutionResult compileTestCode() throws IOException, InterruptedException {
        Path codeJar = CODE_OUTPUT.resolve(String.format("%s.jar", CODE_FILE_NAME));
        Path fullPathToTestFile = CODE_OUTPUT.resolve(String.format("%s.kt", testClassName));
        return compile(fullPathToTestFile, testCode, new Path[]{JarLocations.JUNIT_JAR, codeJar}, testClassName);
    }

    /**
     * Orchestrates code compilation for a single koltin file, including file creation.
     *
     * @param fullPathToFile
     * @param code
     * @param classpath
     * @throws IOException
     * @throws InterruptedException
     * @return
     */
    private ExecutionResult compile(Path fullPathToFile, String code, Path[] classpath, String jarName) throws IOException, InterruptedException {
        KotlinFile.createJavaFile(fullPathToFile, code);
        return  cmdExec.compileCommand(classpath, fullPathToFile, CODE_OUTPUT.resolve(String.format("%s.jar", jarName)));
    }

    /**
     * Orchestrates execution of unit tests for the kotlin project.
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ExecutionResult executeUnitTests() throws IOException, InterruptedException {
        Path codeJar = CODE_OUTPUT.resolve(String.format("%s.jar", CODE_FILE_NAME));
        Path testJar = CODE_OUTPUT.resolve(String.format("%s.jar", testClassName));
        Path[] classpath = new Path[]{JarLocations.JUNIT_JAR, JarLocations.HAMCREST_JAR, codeJar, testJar};
        return cmdExec.executionCommand(CommandExecutor.CodeType.TEST, classpath, testClassName);
    }

    /**
     * Orchestrates execution of the main method for the kotlin project.
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public ExecutionResult executeCode() throws IOException, InterruptedException {
        Path[] classpath = new Path[]{CODE_OUTPUT};
        return cmdExec.executionCommand(CommandExecutor.CodeType.CODE, classpath, CODE_FILE_NAME);
    }
}
