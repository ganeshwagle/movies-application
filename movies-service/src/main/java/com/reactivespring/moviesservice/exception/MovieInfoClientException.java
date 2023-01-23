package com.reactivespring.moviesservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MovieInfoClientException extends RuntimeException {
    String message;
    HttpStatus statusCode;

    public MovieInfoClientException(String message, HttpStatus statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
