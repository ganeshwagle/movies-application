package com.reactivespring.moviesservice.exceptionhandler;

import com.reactivespring.moviesservice.exception.MovieInfoClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieInfoClientException.class)
    public ResponseEntity<String> handleMovieInfoClientException(MovieInfoClientException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }
}
