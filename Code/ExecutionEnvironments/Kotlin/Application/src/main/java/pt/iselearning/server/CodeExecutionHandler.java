package pt.iselearning.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.iselearning.codeExecution.Executor;
import pt.iselearning.exceptions.MissingClassException;
import pt.iselearning.models.Executable;
import pt.iselearning.models.ExecutionResult;

import java.io.IOException;

@RestController
public class CodeExecutionHandler {
    private static final Logger LOGGER = LogManager.getLogger(CodeExecutionHandler.class);

    @PostMapping(value  = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ExecutionResult executeCode(@RequestBody Executable executable) throws MissingClassException, IOException, InterruptedException {
        try {
            Executor exec = new Executor(executable.getCode(), executable.getExecuteTests() ? executable.getUnitTests() : null);
            ExecutionResult compileResult = exec.compileCode();
            if(compileResult.wasError()) {
                return compileResult;
            } else if (executable.getExecuteTests()) {
                return exec.executeUnitTests();
            } else {
                return exec.executeCode();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }
}

