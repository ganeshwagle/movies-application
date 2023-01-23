package com.reactivespring.moviesservice.service;

import com.reactivespring.moviesservice.exception.MovieReviewClientException;
import com.reactivespring.moviesservice.exception.MovieReviewServerException;
import com.reactivespring.moviesservice.model.MovieReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieReviewService {

    @Value("${baseUrl.movieReview}")
    private String movieReviewUrl;

    @Autowired
    private WebClient webClient;

    public Flux<MovieReview> findMovieReviewById(String movieInfoId) {
        return webClient
                .get()
                .uri(UriComponentsBuilder.fromHttpUrl(movieReviewUrl)
                        .queryParam("movieInfoId", movieInfoId)
                        .buildAndExpand()
                        .toUriString()
                )
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(response -> Mono.error(new MovieReviewClientException(response, clientResponse.statusCode()))))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new MovieReviewServerException("Server Exception in Movie Review Service")))
                .bodyToFlux(MovieReview.class);

    }

}
