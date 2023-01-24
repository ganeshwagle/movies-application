package com.reactivespring.moviesservice.controller;

import com.reactivespring.moviesservice.model.Movie;
import com.reactivespring.moviesservice.model.MovieInfo;
import com.reactivespring.moviesservice.service.MovieInfoService;
import com.reactivespring.moviesservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieInfoService movieInfoService;

    @GetMapping("/{movieInfoId}")
    public Mono<Movie> findMovieByMovieId(@PathVariable String movieInfoId) {
        return movieService.buildMovieByMovieInfoId(movieInfoId);
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MovieInfo> movieStream() {
        return movieInfoService.movieInfoStream();
    }

}
