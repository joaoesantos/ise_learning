package codeExecution;

import exceptions.MissingClassException;
import models.ExecutionResult;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import utils.CodeParser;
import utils.CommandLineExecutor;
import utils.JavaFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class is responsible for orchestrating the execution of a java project with a single class and maybe a test
 * class if initialized for that purpose.
 */
public class Executor {
    private final CommandLineExecutor cmdExec;
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

    public Executor(String code, String testCode) throws MissingClassException {
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
        this.cmdExec = CommandLineExecutor.getInstance();
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
        compile(codeClassName, code, new Path[]{});
        if(testCode != null) {
            Path junitJar = Paths.get(".", "src", "main", "resources", "junit-4.13.jar");
            compile(testClassName, testCode, new Path[]{junitJar});
        }
    }

    /**
     * Orchestrates code compilation for a single java file, including file creation.
     *
     * @param codeClassName
     * @param code
     * @param classpathJars
     * @throws IOException
     * @throws InterruptedException
     */
    private void compile(String codeClassName, String code, Path[] classpathJars) throws IOException, InterruptedException {
        Path packagePath = Paths.get(".");
        for (String folder : PACKAGE_NAME.split(".")) {
            packagePath = packagePath.resolve(folder);
        }
        Path fullPathToFile = UNCOMPILED_OUTPUT.resolve(String.format("%s.java", codeClassName));
        JavaFile.createJavaFile(fullPathToFile, code);
        String classpath = mergeClasspathPaths(UNCOMPILED_OUTPUT, classpathJars);
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
        Path junitJar = Paths.get(".", "src", "main", "resources", "junit-4.13.jar");
        Path hamcrestJar = Paths.get(".", "src", "main", "resources", "hamcrest-all-1.3.jar");
        Path[] classpathJars = new Path[]{junitJar, hamcrestJar};
        String classpath = mergeClasspathPaths(COMPILED_OUTPUT, classpathJars);
        String result = cmdExec.executionCommand(CommandLineExecutor.CodeType.TEST, classpath,
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
        String classpath = mergeClasspathPaths(COMPILED_OUTPUT, new Path[]{});
        String result = cmdExec.executionCommand(CommandLineExecutor.CodeType.CODE, classpath,
                String.format("%s.%s", PACKAGE_NAME, codeClassName));
        return new ExecutionResult(result);
    }

    /**
     * Auxiliary method to merge a path with an array of paths into string with format necessary to use as classpath.
     *
     * @param originalClasspath
     * @param otherClasspaths
     * @return
     */
    private String mergeClasspathPaths(Path originalClasspath, Path[] otherClasspaths) {
        String classpath = originalClasspath.toAbsolutePath().toString();
        if(otherClasspaths.length > 0) {
            classpath = Arrays.stream(otherClasspaths).map(cp -> cp.toAbsolutePath().toString())
                    .collect(Collectors.joining(";")) + ";" + classpath;
        }
        return classpath;
    }
}
