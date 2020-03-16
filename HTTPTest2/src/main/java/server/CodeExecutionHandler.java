package server;

import codeExecution.Executor;
import models.Executable;
import models.ExecutionResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CodeExecutionHandler {

    @PostMapping(value  = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ExecutionResult executeCode(@RequestBody Executable executable) throws IOException, InterruptedException {
        Executor exec = new Executor(executable.getCode(), executable.getExecuteTests() ? executable.getUnitTests() : null);
        exec.compileCode();
        if(executable.getExecuteTests()) {
            return exec.executeUnitTests();
        } else {
            return exec.executeCode();
        }
        //return new ExecutionResult("bueda cenas");
    }
}

