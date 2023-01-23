package com.reactivespring.moviesservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MovieReviewClientException extends RuntimeException {

    String message;
    HttpStatus statusCode;

    public MovieReviewClientException(String message, HttpStatus statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
