package com.reactivespring.moviesservice.utils;

import com.reactivespring.moviesservice.exception.MovieInfoServerException;
import com.reactivespring.moviesservice.exception.MovieReviewServerException;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;

import java.time.Duration;

public class RetryUtil {

    public static Retry retryLogic() {
        return Retry.fixedDelay(3, Duration.ofSeconds(1))
                .filter(ex -> ex instanceof MovieInfoServerException ||
                        ex instanceof MovieReviewServerException)
                .onRetryExhaustedThrow( //this is to throw the actual exception
                        (retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())
                );
    }

}
