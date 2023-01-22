package com.reactivespring.moviesreviewservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movie_review")
public class MovieReview {

    @Id
    private String movieReviewId;

    private String movieInfoId;

    private String comment;

    private Double rating;

}
