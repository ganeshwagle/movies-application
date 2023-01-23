package com.reactivespring.moviesservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class MovieInfoClientException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -40049265431290448L;
    String message;
    HttpStatus statusCode;

    public MovieInfoClientException(String message, HttpStatus statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
