package com.reactivespring.moviesservice.exceptionhandler;

import com.reactivespring.moviesservice.exception.MovieInfoClientException;
import com.reactivespring.moviesservice.exception.MovieReviewClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieInfoClientException.class)
    public ResponseEntity<String> handleMovieInfoClientException(MovieInfoClientException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }

    @ExceptionHandler(MovieReviewClientException.class)
    public ResponseEntity<String> handleMovieReviewClientException(MovieReviewClientException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleMovieInfoServerException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
