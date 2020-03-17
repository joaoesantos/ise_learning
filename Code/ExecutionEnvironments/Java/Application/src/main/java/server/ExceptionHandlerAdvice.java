package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import exceptions.MissingClassException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIOException(IOException e) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IO Server Error");
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity handleInterruptedException(InterruptedException e) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing code.");
    }

    @ExceptionHandler(MissingClassException.class)
    public ResponseEntity handleMissingClassException(MissingClassException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
