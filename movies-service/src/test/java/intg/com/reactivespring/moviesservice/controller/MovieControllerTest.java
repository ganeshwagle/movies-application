package com.reactivespring.moviesservice.controller;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8082)
@TestPropertySource(
        properties = {
                "baseUrl.movieInfo=http://localhost:8082/v1/movie-info",
                "baseUrl.movieReview=http://localhost:8082/v1/movie-review"
        }
)
class MovieControllerTest {

}