package com.reactivespring.moviesreviewservice.exception;

public class MovieReviewDataException extends RuntimeException {

    public MovieReviewDataException(String message) {
        super(message);
        this.message = message;
    }
    String message;

}
