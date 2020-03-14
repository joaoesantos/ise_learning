package server;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.junit.runner;

@RestController
public class CodeExecutionHandler {

    @PostMapping(value  = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ExecutionResult executeCode(@RequestBody Executable executable) {
        System.out.println(executable.getCode());
        System.out.println(executable.getUnitTests());
        System.out.println(executable.getExecuteTests());
        return new ExecutionResult("bueda cenas");
    }

}

