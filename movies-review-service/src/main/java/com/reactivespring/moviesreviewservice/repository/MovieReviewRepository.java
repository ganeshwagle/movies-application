package com.reactivespring.moviesreviewservice.repository;

import com.reactivespring.moviesreviewservice.model.MovieReview;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovieReviewRepository extends ReactiveMongoRepository<MovieReview, String> {
    Flux<MovieReview> findByMovieInfoId(String movieInfoId);
}
