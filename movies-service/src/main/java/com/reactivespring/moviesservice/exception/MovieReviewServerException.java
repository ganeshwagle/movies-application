package com.reactivespring.moviesservice.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class MovieReviewServerException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3995302700170355379L;
    String message;

    public MovieReviewServerException(String message) {
        super(message);
        this.message = message;
    }
}
