package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class responsible for executing specific command line commands,
 * which allow to compile and execute java code.
 *
 */
public class CommandLineExecutor {
    private static final Path CMD_OUTPUT_FILE = Paths.get(".", "CMD_Output.txt");
    private static CommandLineExecutor instance;

    private ProcessBuilder processBuilder;

    private CommandLineExecutor(){
        processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.to(
                new File(CMD_OUTPUT_FILE.toString())));
    }

    public static CommandLineExecutor getInstance() {
        if(instance == null) {
            instance = new CommandLineExecutor();
        }
        return instance;
    }

    public enum CodeType { TEST, CODE}

    /**
     * This method executes compile command.
     *
     * @param classpath
     * @param filesPath
     * @param output
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public String compileCommand(String classpath, Path filesPath, Path output) throws InterruptedException, IOException {
        processBuilder.command("cmd.exe", "/c", String.format("javac -cp %s %s -d %s", classpath,
                filesPath.toAbsolutePath().toString(),
                output.toAbsolutePath().toString()));
        Process process = processBuilder.start();
        int exitVal = process.waitFor();
        if (exitVal == 0) {
            return getDataFromCmdResultFile();
        } else {
            return getDataFromCmdResultFile();
        }
    }

    /**
     * This method executes java files, either the unit tests or the main method depending on input parameters.
     *
     * @param type
     * @param classpath
     * @param fullQualifiedClassNameToExecute
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public String executionCommand(CodeType type, String classpath, String fullQualifiedClassNameToExecute)
            throws InterruptedException, IOException {
        if(type.equals(CodeType.TEST)) {
            processBuilder.command("cmd.exe", "/c",
                    String.format("java -cp %s org.junit.runner.JUnitCore %s", classpath, fullQualifiedClassNameToExecute));
        } else if(type.equals(CodeType.CODE)) {
            processBuilder.command("cmd.exe", "/c",
                    String.format("java -cp %s %s", classpath, fullQualifiedClassNameToExecute));
        }
        Process process = processBuilder.start();
        int exitVal = process.waitFor();
        if (exitVal == 0) {
            return getDataFromCmdResultFile();
        } else {
            return getDataFromCmdResultFile();
        }
    }

    /**
     * Auxiliary method to get data from CMD result dump file.
     *
     * @return
     * @throws IOException
     */
    private static String getDataFromCmdResultFile() throws IOException {
        try(Stream<String> lines = Files.lines(CMD_OUTPUT_FILE)) {
            String data = lines.collect(Collectors.joining("\n"));
            return data;
        }
    }
}
