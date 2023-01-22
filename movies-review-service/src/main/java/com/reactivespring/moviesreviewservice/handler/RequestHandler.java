package com.reactivespring.moviesreviewservice.handler;

import com.reactivespring.moviesreviewservice.model.MovieReview;
import com.reactivespring.moviesreviewservice.repository.MovieReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RequestHandler {

    @Autowired
    MovieReviewRepository movieReviewRepository;

    public Mono<ServerResponse> addMovieReview(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MovieReview.class)
                .flatMap(movieReviewRepository::save)
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    public Mono<ServerResponse> getAllMovies(ServerRequest serverRequest) {
        return ServerResponse.ok().body(movieReviewRepository.findAll(), MovieReview.class);
    }


}
