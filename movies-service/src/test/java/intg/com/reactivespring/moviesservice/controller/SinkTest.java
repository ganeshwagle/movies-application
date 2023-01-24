package com.reactivespring.moviesservice.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkTest {

    @Test
    public void replaySink() {
        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> replayIntegerFlux1 = replaySink.asFlux();
        Flux<Integer> replayIntegerFlux2 = replaySink.asFlux();

        replayIntegerFlux1.subscribe(integer -> System.out.println("Subscriber 1 " + integer));
        replayIntegerFlux2.subscribe(integer -> System.out.println("Subscriber 2 " + integer));

        replaySink.tryEmitNext(3);
    }

    @Test
    public void multiCastSink() {
        Sinks.Many<Integer> multiCastSink = Sinks.many().multicast().onBackpressureBuffer();

        multiCastSink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multiCastSink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> replayIntegerFlux1 = multiCastSink.asFlux();
        Flux<Integer> replayIntegerFlux2 = multiCastSink.asFlux();

        replayIntegerFlux1.subscribe(integer -> System.out.println("Subscriber 1 " + integer));
        replayIntegerFlux2.subscribe(integer -> System.out.println("Subscriber 2 " + integer));

        multiCastSink.tryEmitNext(3);
    }

    @Test
    public void uniCastSink() {
        Sinks.Many<Integer> uniCastSink = Sinks.many().unicast().onBackpressureBuffer();

        uniCastSink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        uniCastSink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> replayIntegerFlux1 = uniCastSink.asFlux();

        replayIntegerFlux1.subscribe(integer -> System.out.println("Subscriber 1 " + integer));

        uniCastSink.tryEmitNext(3);
    }
}
