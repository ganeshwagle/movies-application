package com.reactivespring.moviesreviewservice.router;

import com.reactivespring.moviesreviewservice.exceptionHandler.GlobalExceptionHandler;
import com.reactivespring.moviesreviewservice.handler.RequestHandler;
import com.reactivespring.moviesreviewservice.model.MovieReview;
import com.reactivespring.moviesreviewservice.repository.MovieReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest
@AutoConfigureWebTestClient
@ContextConfiguration(classes = {RequestRouter.class, RequestHandler.class, GlobalExceptionHandler.class})
class RequestRouterUnitTest {

    @MockBean
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private WebTestClient webTestClient;

    final String movieReviewUrl = "/v1/movie-review";

    @Test
    void addMovieReview() {

        MovieReview movieReview = new MovieReview(null, "movie-3", "it was awesome right", 10D);

        when(movieReviewRepository.save(isA(MovieReview.class)))
                .thenReturn(Mono.just(new MovieReview("movie-review-id", "movie-3", "it was awesome right", 10D)));

        webTestClient
                .post()
                .uri(movieReviewUrl)
                .bodyValue(movieReview)
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

        when(movieReviewRepository.findAll())
                .thenReturn(Flux.fromIterable(List.of(
                        new MovieReview(null, "movie-1", "it was all right", 5D),
                        new MovieReview(null, "movie-2", "it was ok", 5D),
                        new MovieReview("movie-review-3", "movie-2", "it was awesome right", 8D))));

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

        when(movieReviewRepository.findById(isA(String.class)))
                .thenReturn(Mono.just(new MovieReview("movie-review-3", "movie-2", "it was awesome right", 8D)));

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

        when(movieReviewRepository.save(isA(MovieReview.class)))
                .thenReturn(Mono.just(new MovieReview("movie-review-3", "movie-4", "it was alright", 5D)));

        when(movieReviewRepository.findById(isA(String.class)))
                .thenReturn(Mono.just(
                        new MovieReview("movie-review-3", "movie-2", "it was awesome right", 8D)));

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

        when(movieReviewRepository.deleteById(isA(String.class)))
                .thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(movieReviewUrl + "/" + movieReviewId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void getAllMovieReviewsByMovieInfoId() {

        when(movieReviewRepository.findByMovieInfoId(isA(String.class)))
                .thenReturn(Flux.fromIterable(List.of(
                        new MovieReview(null, "movie-2", "it was ok", 5D),
                        new MovieReview("movie-review-3", "movie-2", "it was awesome right", 8D))));

        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(movieReviewUrl)
                        .queryParam("movieReviewId", "movie-2")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieReview.class)
                .hasSize(2);
    }

    @Test
    void addMovieReviewValidation() {

        MovieReview movieReview = new MovieReview(null, null, "it was awesome right", -10D);

        when(movieReviewRepository.save(isA(MovieReview.class)))
                .thenReturn(Mono.just(new MovieReview("movie-review-id", "movie-3", "it was awesome right", 10D)));

        webTestClient
                .post()
                .uri(movieReviewUrl)
                .bodyValue(movieReview)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    String msg = stringEntityExchangeResult.getResponseBody();
                    assertEquals("MovieReview.movieInfoId can't be null!!!,MovieReview.rating can't be negative!!!", msg);
                });
    }

}