package com.reactivespring.moviesinfoservice.controller;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class MovieInfoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MovieInfoRepository movieInfoRepository;

    final String movieInfoUrl = "/v1/movie-info";

    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var movieList = List.of(
                new MovieInfo(null, "movie-1", dateFormat.parse("2022-08-10"), List.of("actor1", "actor2", "actor3")),
                new MovieInfo(null, "movie-2", dateFormat.parse("2022-08-11"), List.of("actor4", "actor5", "actor6")),
                new MovieInfo(null, "movie-3", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9")));
        movieInfoRepository.saveAll(movieList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll()
                .block();
    }

    @Test
    void addMovieInfo() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        MovieInfo movieInfo = new MovieInfo(null, "movie-3", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9"));

        webTestClient
                .post()
                .uri(movieInfoUrl)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo response = movieInfoEntityExchangeResult.getResponseBody();
                    assert response != null;
                    assertNotNull(response.getMovieInfoId());
                });
    }

}