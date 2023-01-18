package com.reactivespring.moviesinfoservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping(value = "/flux")
    public Flux<Integer> flux() {
        return Flux.just(1, 2, 3, 4, 5);
    }

    @GetMapping(value = "/mono")
    public Mono<String> mono() {
        return Mono.just("hello-world");
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> infinite() {
        return Flux.interval(Duration.ofSeconds(1));
    }
}
