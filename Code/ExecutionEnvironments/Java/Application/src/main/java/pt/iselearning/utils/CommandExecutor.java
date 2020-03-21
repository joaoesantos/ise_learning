package pt.iselearning.utils;

import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class responsible for executing specific command line commands,
 * which allow to compile and execute java code.
 *
 */
public class CommandExecutor {
    private static final Path CMD_OUTPUT_FILE = Paths.get(".", "CMD_Output.txt");
    private static CommandExecutor instance;

    private ProcessBuilder processBuilder;
    private ShellType shellType;

    private enum ShellType {
        BASH("bash", "-c", ":"), CMD("cmd.exe", "/c", ";");

        String exec;
        String c;
        String classpathSeperator;

        ShellType(String exec, String c, String classpathSeperator) {
            this.exec = exec;
            this.c = c;
            this.classpathSeperator = classpathSeperator;
        }
    }

    private CommandExecutor() throws IOException {
        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC){
            this.shellType = ShellType.BASH;
        } else if(SystemUtils.IS_OS_WINDOWS) {
            this.shellType = ShellType.CMD;
        } else {
            this.shellType = ShellType.BASH;
        }
        processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(CMD_OUTPUT_FILE.toFile());
    }

    public static CommandExecutor getInstance() throws IOException {
        if(instance == null) {
            instance = new CommandExecutor();
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
    public String compileCommand(Path[] classpath, Path filesPath, Path output) throws InterruptedException, IOException {
        processBuilder.command(shellType.exec, shellType.c, String.format("javac -cp %s %s -d %s",
                classpathPathArrayToString(classpath), filesPath.toAbsolutePath().toString(),
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
    public String executionCommand(CodeType type, Path[] classpath, String fullQualifiedClassNameToExecute)
            throws InterruptedException, IOException {
        if(type.equals(CodeType.TEST)) {
            processBuilder.command(shellType.exec, shellType.c,
                    String.format("java -cp %s org.junit.runner.JUnitCore %s", classpathPathArrayToString(classpath),
                            fullQualifiedClassNameToExecute));
        } else if(type.equals(CodeType.CODE)) {
            processBuilder.command(shellType.exec, shellType.c,
                    String.format("java -cp %s %s", classpathPathArrayToString(classpath),
                            fullQualifiedClassNameToExecute));
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
            String data = lines.collect(Collectors.joining(System.lineSeparator()));
            return data;
        }
    }

    /**
     * Auxiliary method to convert array of paths into classpath text format.
     *
     * @param paths
     * @return
     */
    private String classpathPathArrayToString(Path[] paths) {
        String classpath = "";
        if(paths.length > 0) {
            classpath = Arrays.stream(paths).map(cp -> cp.toAbsolutePath().toString())
                    .collect(Collectors.joining(shellType.classpathSeperator));
        }
        return classpath;
    }
}
