package com.reactivespring.moviesservice.service;

import com.reactivespring.moviesservice.model.MovieReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

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
                .bodyToFlux(MovieReview.class);

    }

}
