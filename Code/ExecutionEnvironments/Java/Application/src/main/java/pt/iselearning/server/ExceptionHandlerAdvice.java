package pt.iselearning.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pt.iselearning.exceptions.MissingClassException;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIOException(IOException e) {
        LOGGER.debug("handleIOException Exception Handler.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IO Server Error");
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity handleInterruptedException(InterruptedException e) {
        LOGGER.debug("handleInterruptedException Exception Handler.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing code.");
    }

    @ExceptionHandler(MissingClassException.class)
    public ResponseEntity handleMissingClassException(MissingClassException e) {
        LOGGER.debug("handleMissingClassException Exception Handler.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
