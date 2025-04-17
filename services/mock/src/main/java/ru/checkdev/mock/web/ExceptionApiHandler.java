package ru.checkdev.mock.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.checkdev.mock.domain.ErrorMessage;
import ru.checkdev.mock.exception.IdNotFoundException;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(IdNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
}