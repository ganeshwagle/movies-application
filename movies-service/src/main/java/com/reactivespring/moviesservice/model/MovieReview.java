package com.reactivespring.moviesservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieReview {

    private String movieReviewId;

    private String movieInfoId;

    private String comment;

    private Double rating;

}
