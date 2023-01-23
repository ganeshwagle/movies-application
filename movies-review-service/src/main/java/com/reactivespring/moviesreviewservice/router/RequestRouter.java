package com.reactivespring.moviesreviewservice.router;

import com.reactivespring.moviesreviewservice.handler.RequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RequestRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(RequestHandler requestHandler){
        String baseUrl = "/v1/movie-review";
        return route()
                .nest(path(baseUrl), builder -> {
                    builder.POST("", requestHandler::addMovieReview)
                            .GET("", requestHandler::getAllMovies)
                            .GET("/{movieReviewId}", requestHandler::getMovieReviewById)
                            .PUT("/{movieReviewId}", requestHandler::updateMovieReview)
                            .DELETE("/{movieReviewId}", requestHandler::deleteMovieReviewById);
                })
              /*  .POST(baseUrl, requestHandler::addMovieReview)
                .GET(baseUrl, requestHandler::getAllMovies)*/
                .build();
    }

}
