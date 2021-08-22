package edu.spring.euniversity.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ExceptionGlobalHandler extends RuntimeException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return new ResponseEntity<>("\"error\": \"" + e.getMessage() + "\"", HttpStatus.BAD_REQUEST);
    }
}
