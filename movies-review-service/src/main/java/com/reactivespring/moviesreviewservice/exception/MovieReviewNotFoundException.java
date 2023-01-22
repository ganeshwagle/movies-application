package com.reactivespring.moviesreviewservice.exception;

public class MovieReviewNotFoundException extends RuntimeException{
    String message;

    public MovieReviewNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
