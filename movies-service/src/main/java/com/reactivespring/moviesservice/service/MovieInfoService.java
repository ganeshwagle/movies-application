package com.reactivespring.moviesservice.service;

import com.reactivespring.moviesservice.model.MovieInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
                .bodyToMono(MovieInfo.class);
    }

}
