package com.reactivespring.moviesinfoservice.controller;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.service.MovieInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class MovieInfoController {

    MovieInfoService movieInfoService;

    public MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping("/movie-info")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> saveMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.saveMovieInfo(movieInfo);
    }

    @GetMapping(value = "/movie-info", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieInfo> getAllMovieInfo(@RequestParam(required = false, value = "year") String movieName) {
        if(movieName != null && !movieName.isBlank())
            return movieInfoService.findByMovieName(movieName);
        return movieInfoService.getAllMovieInfo();
    }

    @GetMapping(value = "/movie-info/{movieInfoId}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String movieInfoId) {
        return movieInfoService.getMovieInfoById(movieInfoId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping(value = "/movie-info/{movieInfoId}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfoById(@RequestBody MovieInfo movieInfo, @PathVariable String movieInfoId) {
        return movieInfoService.updateMovieInfoById(movieInfoId, movieInfo)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping(value = "/movie-info/{movieInfoId}")
    public Mono<String> deleteMovieInfoById(@PathVariable String movieInfoId) {
        return movieInfoService.deleteMovieInfoById(movieInfoId);
    }

}
