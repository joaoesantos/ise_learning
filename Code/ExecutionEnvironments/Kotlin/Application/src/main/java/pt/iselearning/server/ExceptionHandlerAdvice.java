package pt.iselearning.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pt.iselearning.exceptions.ApplicationException;
import pt.iselearning.exceptions.CommandExecutionTimeout;
import pt.iselearning.exceptions.MissingClassException;
import pt.iselearning.models.ProblemJsonModel;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlerAdvice.class);
    private static HttpHeaders httpHeaders = new HttpHeaders();
    static {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    }

    @ExceptionHandler(MissingClassException.class)
    public ResponseEntity<ProblemJsonModel> handleMissingClassException(MissingClassException e) {
        LOGGER.debug("handleMissingClassException Exception Handler.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(httpHeaders)
                .body(new ProblemJsonModel(e.getType(), e.getTitle(), e.getMessage(), e.getInstance()));
    }

    @ExceptionHandler(CommandExecutionTimeout.class)
    public ResponseEntity<ProblemJsonModel> handleTimeoutException(ApplicationException e) {
        LOGGER.debug("handleException Timeout Exception Handler.");
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).headers(httpHeaders)
                .body(new ProblemJsonModel(e.getType(), e.getTitle(), e.getMessage(), e.getInstance()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ProblemJsonModel> handleException(ApplicationException e) {
        LOGGER.debug("handleException Exception Handler.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders)
                .body(new ProblemJsonModel(e.getType(), e.getTitle(), e.getMessage(), e.getInstance()));
    }
}
