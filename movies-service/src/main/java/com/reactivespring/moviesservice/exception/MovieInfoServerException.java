package com.reactivespring.moviesservice.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class MovieInfoServerException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3637953011120388357L;
    String message;

    public MovieInfoServerException(String message) {
        super(message);
        this.message = message;
    }
}
