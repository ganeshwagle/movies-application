package com.reactivespring.moviesinfoservice.repository;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@DataMongoTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var movieList = List.of(
                new MovieInfo(null, "movie-1", dateFormat.parse("2022-08-10"), List.of("actor1", "actor2", "actor3")),
                new MovieInfo(null, "movie-2", dateFormat.parse("2022-08-11"), List.of("actor4", "actor5", "actor6")),
                new MovieInfo(null, "movie-3", dateFormat.parse("2023-08-12"), List.of("actor7", "actor8", "actor9")));
        movieInfoRepository.saveAll(movieList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll()
                .block();
    }

    @Test
    void findAll() {
        var movieFlux = movieInfoRepository.findAll().log();

        StepVerifier.create(movieFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findAllByMovieName() {
        var movieFlux = movieInfoRepository.findByMovieName("movie-1").log();

        StepVerifier.create(movieFlux)
                .expectNextCount(1)
                .verifyComplete();
    }
}