package com.reactivespring.moviesreviewservice.router;

import com.reactivespring.moviesreviewservice.model.MovieReview;
import com.reactivespring.moviesreviewservice.repository.MovieReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("movie-review-test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class RequestRouterIntgTest {

    final String movieReviewUrl = "/v1/movie-review";
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    MovieReviewRepository movieReviewRepository;

    @BeforeEach
    void setUp() {
        var movieList = List.of(
                new MovieReview(null, "movie-1", "it was all right", 5D),
                new MovieReview(null, "movie-2", "it was ok", 5D),
                new MovieReview("movie-review-3", "movie-3", "it was awesome right", 8D));
        movieReviewRepository.saveAll(movieList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieReviewRepository.deleteAll().block();
    }

    @Test
    void addMovieReview() {

        MovieReview movieInfo = new MovieReview(null, "movie-3", "it was awesome right", 10D);

        webTestClient
                .post()
                .uri(movieReviewUrl)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieReview.class)
                .consumeWith(movieReviewEntityExchangeResult -> {
                    MovieReview response = movieReviewEntityExchangeResult.getResponseBody();
                    assert response != null;
                    assertNotNull(response.getMovieReviewId());
                });
    }

    @Test
    void getAllMovieReviews() {
        webTestClient
                .get()
                .uri(movieReviewUrl)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieReview.class)
                .hasSize(3);
    }

    @Test
    void getMovieReviewById() {
        String movieReviewId = "movie-review-3";
        webTestClient
                .get()
                .uri(movieReviewUrl + "/" + movieReviewId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieReview.class)
                .consumeWith(movieReviewEntityExchangeResult -> {
                    MovieReview movieReview = movieReviewEntityExchangeResult.getResponseBody();
                    assert movieReview != null;
                    assertEquals(movieReviewId, movieReview.getMovieReviewId());
                });
    }

    @Test
    void updateMovieReviewById() {
        String movieReviewId = "movie-review-3";
        MovieReview updatedMovieReview = new MovieReview(null, "movie-4", "it was alright", 5D);
        webTestClient
                .put()
                .uri(movieReviewUrl + "/" + movieReviewId)
                .bodyValue(updatedMovieReview)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieReview.class)
                .consumeWith(movieReviewEntityExchangeResult -> {
                    MovieReview movieReview = movieReviewEntityExchangeResult.getResponseBody();
                    assert movieReview != null;
                    assertEquals(movieReviewId, movieReview.getMovieReviewId());
                    assertEquals("it was alright", movieReview.getComment());
                });
    }

    @Test
    void deleteMovieReviewById() {
        String movieReviewId = "movie-review-3";
        webTestClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(movieReviewUrl)
                        .queryParam("movieReviewId", movieReviewId)
                        .build())
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}