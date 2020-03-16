package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineExecutor {
    private static CommandLineExecutor instance;

    private ProcessBuilder processBuilder;

    private CommandLineExecutor(){
        processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(
                new File(Paths.get(".").resolve("CMD_Output.txt").toString())));
    }

    public static CommandLineExecutor getInstance() {
        if(instance == null) {
            instance = new CommandLineExecutor();
        }
        return instance;
    }

    public enum CodeType { TEST, CODE}

    public void compileCommand(String classpath, Path filesPath, Path output) throws InterruptedException, IOException {
        processBuilder.command("cmd.exe", "/c", String.format("javac -cp %s %s -d %s", classpath,
                filesPath.toAbsolutePath().toString(),
                output.toAbsolutePath().toString()));
        Process process = processBuilder.start();
        int exitVal = process.waitFor();
        if (exitVal == 0) {
            System.out.println("Success!");
        } else {
            System.out.println("Shit!");
            //deu merda ao compilar
        }
    }

    public void executionCommand(CodeType type) {
        if(type.equals(CodeType.CODE)) {

        } else if(type.equals(CodeType.TEST)) {

        }
    }
}
