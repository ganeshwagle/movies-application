package com.reactivespring.moviesinfoservice.service;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    MovieInfoRepository movieInfoRepository;

    public MovieInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> saveMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }


    public Flux<MovieInfo> getAllMovieInfo() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieInfoById(String movieInfoId) {
        return movieInfoRepository.findById(movieInfoId);
    }

    public Mono<MovieInfo> updateMovieInfoById(String movieInfoId, MovieInfo movieInfo) {
        return movieInfoRepository.findById(movieInfoId)
                .flatMap(movieInfoFromDb -> {
                    if (movieInfo.getMovieName() != null
                            && !movieInfo.getMovieName().isBlank())
                        movieInfoFromDb.setMovieName(movieInfo.getMovieName());
                    if (movieInfo.getReleaseDate() != null)
                        movieInfoFromDb.setReleaseDate(movieInfo.getReleaseDate());
                    if (movieInfo.getCast() != null
                            && !movieInfo.getCast().isEmpty())
                        movieInfoFromDb.setCast(movieInfo.getCast());
                    return movieInfoRepository.save(movieInfoFromDb);
                });
    }

    public Mono<String> deleteMovieInfoById(String movieInfoId) {
        return movieInfoRepository.findById(movieInfoId)
                .flatMap(movieInfo -> {
                    movieInfoRepository.deleteById(movieInfoId)
                            .subscribe();
                    return Mono.just("MovieInfo Deleted Successfully!!!");
                })
                .switchIfEmpty(Mono.just("Invalid id!!!"));
    }
}
