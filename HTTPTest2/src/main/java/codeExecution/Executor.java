package codeExecution;

import models.ExecutionResult;
import utils.CodeParser;
import utils.CommandLineExecutor;
import utils.JavaFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Executor {
    private String codeClassName;
    private String code;

    private String testClassName;
    private String testCode;

    private final static String PACKAGE_NAME = "app";
    private final static Path BASE_FILE_PATH = Paths.get(".", "src", "main", "resources", "template");
    private final static Path OUTPUT_PATH = Paths.get(".", "output");

    public Executor(String code, String testCode) {
        this.code = String.format("package %s;", PACKAGE_NAME) + CodeParser.removeEndLinesAndDuplicateSpaces(code);
        this.codeClassName = CodeParser.extractClassName(this.code);
        //prever situação de nao conseguir extrair class name
        if(testCode != null) {
            this.testCode = String.format("package %s;", PACKAGE_NAME) + CodeParser.removeEndLinesAndDuplicateSpaces(testCode);
            this.testClassName = CodeParser.extractClassName(this.testCode);
            //prever situação de nao conseguir extrair class name
        }
    }

    public void compileCode() throws IOException, InterruptedException {
        compile(codeClassName, code, new Path[]{});
        if(testCode != null) {
            Path junitJar = Paths.get(".", "src", "main", "resources", "junit-4.13.jar");
            compile(testClassName, testCode, new Path[]{junitJar});
        }
    }

    private void compile(String codeClassName, String code, Path[] classpathJars) throws IOException, InterruptedException {
        Path packagePath = Paths.get(".");
        for (String folder : PACKAGE_NAME.split(".")) {
            packagePath = packagePath.resolve(folder);
        }
        Path fullPathToFile = BASE_FILE_PATH.resolve(String.format("%s.java", codeClassName));
        JavaFile.createJavaFile(fullPathToFile, code);
        CommandLineExecutor cmdExec = CommandLineExecutor.getInstance();

        String classpath = BASE_FILE_PATH.toAbsolutePath().toString();
        if(classpathJars.length > 0) {
            classpath = Arrays.stream(classpathJars).map(cp -> cp.toAbsolutePath().toString())
                    .collect(Collectors.joining(";")) + ";" + classpath;
        }

        cmdExec.compileCommand(classpath, fullPathToFile, OUTPUT_PATH);
        //prever situaçao em que compilação falha
    }

    public ExecutionResult executeUnitTests() {
        return null;
    }

    public ExecutionResult executeCode() {
        return null;
    }
}
