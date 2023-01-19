package com.reactivespring.moviesinfoservice.controller;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
class MovieInfoControllerUnitTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    MovieInfoService movieInfoServiceMock;

    final String movieInfoUrl = "/v1/movie-info";

    @Test
    void getAllMovieInfo() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        when(movieInfoServiceMock.getAllMovieInfo())
                .thenReturn(Flux.fromIterable(List.of(
                        new MovieInfo(null, "movie-1", dateFormat.parse("2022-08-10"), List.of("actor1", "actor2", "actor3")),
                        new MovieInfo(null, "movie-2", dateFormat.parse("2022-08-11"), List.of("actor4", "actor5", "actor6")),
                        new MovieInfo("movie-3-id", "movie-3", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9")))));
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        when(movieInfoServiceMock.getMovieInfoById(isA(String.class)))
                .thenReturn(Mono.just(
                        new MovieInfo("movie-3-id", "movie-3", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9"))));

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
    void addMovieInfo() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        MovieInfo movieInfo = new MovieInfo("mockId", "movie-3", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9"));

        when(movieInfoServiceMock.saveMovieInfo(isA(MovieInfo.class)))
                .thenReturn(Mono.just(movieInfo));

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
                    assertEquals("mockId", response.getMovieInfoId());
                });
    }

    @Test
    void updateMovieInfoById() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String movieInfoId = "movie-3-id";
        MovieInfo updatedMovieInfo = new MovieInfo("movie-3-id", "movie-3-modified", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9"));

        when(movieInfoServiceMock.updateMovieInfoById(isA(String.class),isA(MovieInfo.class)))
                .thenReturn(Mono.just(updatedMovieInfo));
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

        when(movieInfoServiceMock.deleteMovieInfoById(isA(String.class)))
                .thenReturn(Mono.just("MovieInfo Deleted Successfully!!!"));

        webTestClient
                .delete()
                .uri(movieInfoUrl + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var msg = stringEntityExchangeResult.getResponseBody();
                    assertEquals("MovieInfo Deleted Successfully!!!", msg);
                });
    }

    @Test
    void addMovieInfoValidationException() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        MovieInfo movieInfo = new MovieInfo("mockId", "", dateFormat.parse("2022-08-12"), List.of("actor7", "actor8", "actor9"));

        webTestClient
                .post()
                .uri(movieInfoUrl)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    String response = movieInfoEntityExchangeResult.getResponseBody();
                    assert response != null;
                    assertEquals("MovieInfo.movieName can't be empty", response);
                });
    }

    @Test
    void addMovieInfoCastValidation() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        MovieInfo movieInfo = new MovieInfo("mockId", "movie-3", dateFormat.parse("2022-08-12"), List.of(""));

        webTestClient
                .post()
                .uri(movieInfoUrl)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    String res = stringEntityExchangeResult.getResponseBody();
                    assertNotNull(res);
                });
    }


}