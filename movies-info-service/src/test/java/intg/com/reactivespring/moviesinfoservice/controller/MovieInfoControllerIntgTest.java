package com.reactivespring.moviesinfoservice.controller;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class MovieInfoControllerIntgTest {

    final String movieInfoUrl = "/v1/movie-info";
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var movieList = List.of(
                new MovieInfo(null, "movie-1", dateFormat.parse("2022-08-10"), List.of("actor1", "actor2", "actor3")),
                new MovieInfo(null, "movie-2", dateFormat.parse("2022-08-11"), List.of("actor4", "actor5", "actor6")),
                new MovieInfo("movie-3-id", "movie-3", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9")));
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

    @Test
    void getAllMovieInfo() throws ParseException {

        webTestClient
                .get()
                .uri(movieInfoUrl)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void getMovieInfoById() throws ParseException {
        String movieInfoId = "movie-3-id";
        webTestClient
                .get()
                .uri(movieInfoUrl + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo movieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert movieInfo != null;
                    assertEquals(movieInfoId, movieInfo.getMovieInfoId());
                });
    }

    @Test
    void updateMovieInfoById() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String movieInfoId = "movie-3-id";
        MovieInfo updatedMovieInfo = new MovieInfo("movie-3-id", "movie-3-modified", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9"));
        webTestClient
                .put()
                .uri(movieInfoUrl + "/{id}", movieInfoId)
                .bodyValue(updatedMovieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo movieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert movieInfo != null;
                    assertEquals(movieInfoId, movieInfo.getMovieInfoId());
                    assertEquals("movie-3-modified", movieInfo.getMovieName());
                });
    }

    @Test
    void deleteMovieInfoById() throws ParseException {
        String movieInfoId = "movie-3-id";
        webTestClient
                .delete()
                .uri(movieInfoUrl + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    String res = stringEntityExchangeResult.getResponseBody();
                    assertEquals("MovieInfo Deleted Successfully!!!", res);
                });
    }

    @Test
    void updateMovieInfoByIdNotFound() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String movieInfoId = "invalid-id";
        MovieInfo updatedMovieInfo = new MovieInfo("movie-3-id", "movie-3-modified", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9"));
        webTestClient
                .put()
                .uri(movieInfoUrl + "/{id}", movieInfoId)
                .bodyValue(updatedMovieInfo)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getMovieInfoByIdNotFound() throws ParseException {
        String movieInfoId = "invalid-id";
        webTestClient
                .get()
                .uri(movieInfoUrl + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}