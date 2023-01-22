package com.reactivespring.moviesreviewservice.repository;

import com.reactivespring.moviesreviewservice.model.MovieReview;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieReviewRepository extends ReactiveMongoRepository<MovieReview, String> {
}
