package com.reactivespring.moviesreviewservice.handler;

import com.reactivespring.moviesreviewservice.exception.MovieReviewDataException;
import com.reactivespring.moviesreviewservice.model.MovieReview;
import com.reactivespring.moviesreviewservice.repository.MovieReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestHandler {

    @Autowired
    private Validator validator;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    public Mono<ServerResponse> addMovieReview(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MovieReview.class)
                .doOnNext(this::validate)
                .flatMap(movieReviewRepository::save)
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    private void validate(MovieReview movieReview) {
        Set<ConstraintViolation<MovieReview>> constraintViolations = validator.validate(movieReview);
        if (constraintViolations.size() > 0) {
            String message = constraintViolations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            throw new MovieReviewDataException(message);
        }

    }

    public Mono<ServerResponse> getAllMovies(ServerRequest serverRequest) {
        Optional<String> movieInfoIdOpt = serverRequest.queryParam("movieReviewId");
        return movieInfoIdOpt.map(s -> ServerResponse.ok().body(movieReviewRepository.findByMovieInfoId(s), MovieReview.class)).orElseGet(() -> ServerResponse.ok().body(movieReviewRepository.findAll(), MovieReview.class));
    }


    public Mono<ServerResponse> getMovieReviewById(ServerRequest serverRequest) {
        String movieReviewId = serverRequest.pathVariable("movieReviewId");
        return ServerResponse.ok().body(movieReviewRepository.findById(movieReviewId), MovieReview.class);
    }

    public Mono<ServerResponse> updateMovieReview(ServerRequest serverRequest) {
        String movieReviewId = serverRequest.pathVariable("movieReviewId");
        /*return serverRequest.bodyToMono(MovieReview.class)
                .zipWith(movieReviewRepository.findById(movieReviewId))
                .flatMap(tuple -> {
                    MovieReview movieReview = tuple.getT1();
                    MovieReview movieReviewInDb = tuple.getT2();
                    if (movieReview.getMovieInfoId() != null && !movieReview.getMovieInfoId().isBlank())
                        movieReviewInDb.setMovieInfoId(movieReview.getMovieInfoId());
                    if (movieReview.getComment() != null && !movieReview.getComment().isBlank())
                        movieReviewInDb.setComment(movieReview.getComment());
                    if (movieReview.getRating() != null)
                        movieReviewInDb.setRating(movieReview.getRating());
                    return movieReviewRepository.save(movieReviewInDb);
                })
                .flatMap(ServerResponse.ok()::bodyValue)
                .switchIfEmpty(ServerResponse.noContent().build());*/
        return serverRequest.bodyToMono(MovieReview.class)
                .flatMap(movieReview -> movieReviewRepository
                        .findById(movieReviewId)
                        .flatMap(movieReviewInDb -> {
                            if (movieReview.getMovieInfoId() != null && !movieReview.getMovieInfoId().isBlank())
                                movieReviewInDb.setMovieInfoId(movieReview.getMovieInfoId());
                            if (movieReview.getComment() != null && !movieReview.getComment().isBlank())
                                movieReviewInDb.setComment(movieReview.getComment());
                            if (movieReview.getRating() != null)
                                movieReviewInDb.setRating(movieReview.getRating());
                            return movieReviewRepository.save(movieReviewInDb);
                        })
                )
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteMovieReviewById(ServerRequest serverRequest) {
        String movieReviewId = serverRequest.pathVariable("movieReviewId");
        return movieReviewRepository.deleteById(movieReviewId)
                .then(ServerResponse.noContent().build());
    }
}
