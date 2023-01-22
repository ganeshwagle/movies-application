package com.reactivespring.moviesreviewservice.exceptionHandler;

import com.reactivespring.moviesreviewservice.exception.MovieReviewDataException;
import com.reactivespring.moviesreviewservice.exception.MovieReviewNotFoundException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer message = dataBufferFactory.wrap(ex.getMessage().getBytes());

        if (ex instanceof MovieReviewDataException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().writeWith(Mono.just(message));
        }
        if(ex instanceof MovieReviewNotFoundException){
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().writeWith(Mono.just(message));
        }
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return exchange.getResponse().writeWith(Mono.just(message));
    }
}
