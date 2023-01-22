package com.reactivespring.moviesreviewservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movie_review")
public class MovieReview {

    @Id
    private String movieReviewId;

    @NotNull(message = "MovieReview.movieInfoId can't be null!!!")
    private String movieInfoId;

    private String comment;

    @Min(value = 0, message = "MovieReview.rating can't be negative!!!")
    private Double rating;

}
