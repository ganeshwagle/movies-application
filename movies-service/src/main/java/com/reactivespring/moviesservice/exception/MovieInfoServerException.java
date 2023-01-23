package com.reactivespring.moviesservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MovieInfoServerException extends RuntimeException {
    String message;
    HttpStatus statusCode;

    public MovieInfoServerException(String message, HttpStatus statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
