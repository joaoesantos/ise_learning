package pt.iselearning.utils;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import pt.iselearning.exceptions.CommandExecutionTimeout;
import pt.iselearning.models.ExecutionResult;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class responsible for executing specific command line commands,
 * which allow to compile and execute java code.
 *
 */
public class CommandExecutor implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger(CommandExecutor.class);
    private final Path CMD_OUTPUT_DIR = Paths.get(".", UUID.randomUUID().toString());
    private final Path CMD_OUTPUT_FILE =CMD_OUTPUT_DIR.resolve("CMD_Output.txt");

    private ProcessBuilder processBuilder;
    private ShellType shellType;

    private long timeout;

    @Override
    public void close() throws Exception {
        if(CMD_OUTPUT_DIR.toFile().exists()) {
            FileUtils.deleteDirectory(CMD_OUTPUT_DIR.toFile());
        }
    }

    private enum ShellType {
        BASH("bash", "-c", ":"), CMD("cmd.exe", "/c", ";");

        String exec;
        String c;
        String classpathSeparator;

        ShellType(String exec, String c, String classpathSeparator) {
            this.exec = exec;
            this.c = c;
            this.classpathSeparator = classpathSeparator;
        }
    }

    public CommandExecutor() throws IOException {
        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
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
        if(!CMD_OUTPUT_DIR.toFile().exists()) {
            CMD_OUTPUT_DIR.toFile().mkdirs();
        }
        processBuilder.redirectOutput(CMD_OUTPUT_FILE.toFile());

        Properties prop = new Properties();
        String propFileName = "props.properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            prop.load(inputStream);
            this.timeout = Long.parseLong(prop.getProperty("execution.timeout"));
        }
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
        String commandText = String.format("javac%s %s -d %s",
                (classpath == null || classpath.length == 0)
                        ? "" : " -cp " + classpathPathArrayToString(classpath),
                filesPath.toAbsolutePath().toString(), output.toAbsolutePath().toString());
        LOGGER.info(String.format("Execute compile command %s", commandText));
        processBuilder.command(shellType.exec, shellType.c, commandText);
        Process process = processBuilder.start();
        int exitVal = process.waitFor();
        return getExecutionResult(exitVal, 0);
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
    public ExecutionResult executionCommand(CodeType type, Path[] classpath, String fullQualifiedClassNameToExecute)
            throws InterruptedException, IOException, CommandExecutionTimeout {
        if(type.equals(CodeType.TEST)) {
            String executeTestCommandText = String.format("java -cp %s org.junit.runner.JUnitCore %s",
                    classpathPathArrayToString(classpath), fullQualifiedClassNameToExecute);
            LOGGER.info(String.format("Execute execute tests command %s", executeTestCommandText));
            processBuilder.command(shellType.exec, shellType.c, executeTestCommandText);
        } else if(type.equals(CodeType.CODE)) {
            String executeCodeCommandText = String.format("java -cp %s %s", classpathPathArrayToString(classpath),
                    fullQualifiedClassNameToExecute);
            LOGGER.info(String.format("Execute execute code command %s", executeCodeCommandText));
            processBuilder.command(shellType.exec, shellType.c, executeCodeCommandText);
        }
        long start = System.currentTimeMillis();
        Process process = processBuilder.start();
        boolean wasTimeout = !process.waitFor(this.timeout, TimeUnit.SECONDS);
        long end = System.currentTimeMillis();
        if(wasTimeout) {
            throw new CommandExecutionTimeout(
                    "TimeoutExpired",
                    "TimeoutExpired",
                    String.format("The command execution timed out, took more than %d seconds", this.timeout),
                    "/execute/java/timeout"
            );
        }
        int exitVal = process.exitValue();
        return getExecutionResult(exitVal, end-start);
    }

    /**
     * Auxiliary method to get Execution result depending of value returned from shell.
     *
     * @param exitVal
     * @param duration
     * @return
     * @throws IOException
     */
    public ExecutionResult getExecutionResult(int exitVal, long duration) throws IOException {
        if (exitVal == 0) {
            LOGGER.info("Command executed successfuly.");
            return new ExecutionResult(getDataFromCmdResultFile(), false, duration);
        } else {
            LOGGER.error("Command executed with error.");
            return new ExecutionResult(getDataFromCmdResultFile(), true, duration);
        }
    }

    /**
     * Auxiliary method to get data from CMD result dump file.
     *
     * @return
     * @throws IOException
     */
    private String getDataFromCmdResultFile() throws IOException {
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
                    .collect(Collectors.joining(shellType.classpathSeparator));
        }
        return classpath;
    }
}
