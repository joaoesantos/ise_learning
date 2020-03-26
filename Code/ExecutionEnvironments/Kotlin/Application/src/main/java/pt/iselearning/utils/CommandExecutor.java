package pt.iselearning.utils;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.iselearning.models.ExecutionResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class responsible for executing specific command line commands,
 * which allow to compile and execute koltin code.
 *
 */
public class CommandExecutor {
    private static final Logger LOGGER = LogManager.getLogger(CommandExecutor.class);
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
            LOGGER.debug("Linux or Mac environment detected.");
            this.shellType = ShellType.BASH;
        } else if(SystemUtils.IS_OS_WINDOWS) {
            LOGGER.debug("Windows environment detected.");
            this.shellType = ShellType.CMD;
        } else {
            LOGGER.debug("Other environment detected.");
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
    public ExecutionResult compileCommand(Path[] classpath, Path filesPath, Path output) throws InterruptedException, IOException {
        String commandText = String.format("kotlinc%s %s -include-runtime -d %s",
                (classpath == null || classpath.length == 0)
                        ? "" : " -classpath " + classpathPathArrayToString(classpath),
                filesPath.toAbsolutePath().toString(), output.toAbsolutePath().toString());
        LOGGER.info(String.format("Execute compile command %s", commandText));
        processBuilder.command(shellType.exec, shellType.c, commandText);
        Process process = processBuilder.start();
        int exitVal = process.waitFor();
        return getExecutionResult(exitVal);
    }

    /**
     * This method executes koltin files, either the unit tests or the main method depending on input parameters.
     *
     * @param type
     * @param classpath
     * @param fullQualifiedClassNameToExecute
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public ExecutionResult executionCommand(CodeType type, Path[] classpath, String fullQualifiedClassNameToExecute)
            throws InterruptedException, IOException {
        if(type.equals(CodeType.TEST)) {
            String executeTestCommandText = String.format("java -cp %s org.junit.runner.JUnitCore %s",
                    classpathPathArrayToString(classpath), fullQualifiedClassNameToExecute);
            LOGGER.info(String.format("Execute execute tests command %s", executeTestCommandText));
            processBuilder.command(shellType.exec, shellType.c, executeTestCommandText);
        } else if(type.equals(CodeType.CODE)) {
            String executeCodeCommandText = String.format("java -jar %s.jar",
                    classpath[0].resolve(fullQualifiedClassNameToExecute));
            LOGGER.info(String.format("Execute execute code command %s", executeCodeCommandText));
            processBuilder.command(shellType.exec, shellType.c, executeCodeCommandText);
        }
        Process process = processBuilder.start();
        int exitVal = process.waitFor();
        return getExecutionResult(exitVal);
    }

    /**
     * Auxiliary method to get Execution result depending of value returned from shell.
     *
     * @param exitVal
     * @return
     * @throws IOException
     */
    public ExecutionResult getExecutionResult(int exitVal) throws IOException {
        if (exitVal == 0) {
            LOGGER.info("Command executed successfuly.");
            return new ExecutionResult(getDataFromCmdResultFile(), false);
        } else {
            LOGGER.error("Command executed with error.");
            return new ExecutionResult(getDataFromCmdResultFile(), true);
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
