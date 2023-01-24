package com.reactivespring.moviesservice.service;

import com.reactivespring.moviesservice.exception.MovieInfoClientException;
import com.reactivespring.moviesservice.exception.MovieInfoServerException;
import com.reactivespring.moviesservice.model.MovieInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    @Value("${baseUrl.movieInfo}")
    private String movieInfoUrl;

    @Autowired
    private WebClient webClient;

    public Mono<MovieInfo> findMovieInfoById(String movieInfoId) {
        String url = movieInfoUrl + "/{movieInfoId}";
        return webClient
                .get()
                .uri(url, movieInfoId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new MovieInfoClientException("Invalid movie info Id!!!", HttpStatus.NOT_FOUND));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono.error(new MovieInfoClientException(response, clientResponse.statusCode())));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new MovieInfoServerException("Server Exception in Movie Info Service")))
                .bodyToMono(MovieInfo.class)
                .retry(3);
    }

}
