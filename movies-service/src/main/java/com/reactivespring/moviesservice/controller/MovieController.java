package com.reactivespring.moviesservice.controller;

import com.reactivespring.moviesservice.model.Movie;
import com.reactivespring.moviesservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{movieInfoId}")
    public Mono<Movie> findMovieByMovieId(@PathVariable String movieInfoId) {
        return movieService.buildMovieByMovieInfoId(movieInfoId);
    }

}
