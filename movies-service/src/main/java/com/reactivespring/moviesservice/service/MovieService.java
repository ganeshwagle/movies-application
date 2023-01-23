package com.reactivespring.moviesservice.service;

import com.reactivespring.moviesservice.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MovieService {

    @Autowired
    private MovieInfoService movieInfoService;

    @Autowired
    private MovieReviewService movieReviewService;

    public Mono<Movie> buildMovieByMovieInfoId(String movieInfoId) {
        return movieInfoService.findMovieInfoById(movieInfoId)
                .flatMap(movieInfo ->
                        movieReviewService.findMovieReviewById(movieInfoId)
                                .collectList()
                                .map(movieReview -> new Movie(movieInfo, movieReview))
                );
    }

}
