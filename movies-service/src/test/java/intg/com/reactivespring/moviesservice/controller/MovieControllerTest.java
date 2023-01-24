package com.reactivespring.moviesservice.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.reactivespring.moviesservice.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8085)
@TestPropertySource(
        properties = {
                "wiremock.server.httpsPort=-1",
                "baseUrl.movieInfo=http://localhost:8085/v1/movie-info",
                "baseUrl.movieReview=http://localhost:8085/v1/movie-review"
        }
)
class MovieControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void getMovieById() {
        String movieInfoId = "63ccb87be92d876d481dbf8d";
        stubFor(
                get(urlEqualTo("/v1/movie-info/" + movieInfoId))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("movieInfo.json")));
        stubFor(
                get(urlPathEqualTo("/v1/movie-review"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("movieReview.json")));
        webTestClient
                .get()
                .uri("/v1/movies/" + movieInfoId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    Movie movie = movieEntityExchangeResult.getResponseBody();
                    System.out.println(movie);
                });

    }

    @Test
    public void getMovieByIdWith404() {
        String movieInfoId = "63ccb87be92d876d481dbf8d";
        stubFor(
                get(urlEqualTo("/v1/movie-info/" + movieInfoId))
                        .willReturn(aResponse().withStatus(404)));
        stubFor(
                get(urlPathEqualTo("/v1/movie-review"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("movieReview.json")));
        webTestClient
                .get()
                .uri("/v1/movies/" + movieInfoId)
                .exchange()
                .expectStatus()
                .is4xxClientError();

    }

    @Test
    public void getMovieByIdWith500ForRetry() {
        String movieInfoId = "63ccb87be92d876d481dbf8d";
        stubFor(
                get(urlEqualTo("/v1/movie-info/" + movieInfoId))
                        .willReturn(aResponse().withStatus(500)));
        webTestClient
                .get()
                .uri("/v1/movies/" + movieInfoId)
                .exchange()
                .expectStatus()
                .is5xxServerError();

        WireMock.verify(4, getRequestedFor(urlEqualTo("/v1/movie-info/" + movieInfoId)));

    }

}