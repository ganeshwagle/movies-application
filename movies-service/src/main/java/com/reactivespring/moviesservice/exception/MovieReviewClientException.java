package com.reactivespring.moviesservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class MovieReviewClientException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4311482494462166304L;
    String message;
    HttpStatus statusCode;

    public MovieReviewClientException(String message, HttpStatus statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
