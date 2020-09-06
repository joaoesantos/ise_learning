package pt.iselearning.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.iselearning.codeExecution.Executor;
import pt.iselearning.exceptions.ApplicationException;
import pt.iselearning.exceptions.CommandExecutionTimeout;
import pt.iselearning.exceptions.MissingClassException;
import pt.iselearning.models.Executable;
import pt.iselearning.models.ExecutionResult;

import java.io.IOException;

@RestController
public class CodeExecutionHandler {
    private static final Logger LOGGER = LogManager.getLogger(CodeExecutionHandler.class);

    @PostMapping(value  = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ExecutionResult executeCode(@RequestBody Executable executable) throws ApplicationException {
        try {
            Executor exec = new Executor(executable.getCode(), executable.getExecuteTests() ? executable.getUnitTests() : null);
            ExecutionResult compileResult = exec.compileCode();
            if(compileResult.getWasError()) {
                return compileResult;
            } else if (executable.getExecuteTests()) {
                return exec.executeUnitTests();
            } else {
                return exec.executeCode();
            }
        } catch (ApplicationException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ApplicationException(
                    "Internal Server Error",
                    "Internal Server Error",
                    e.getMessage(),
                    String.format("/execute/java/%s/error", executable.getExecuteTests() ? "tests" : "code")
            );
        }
    }
}

