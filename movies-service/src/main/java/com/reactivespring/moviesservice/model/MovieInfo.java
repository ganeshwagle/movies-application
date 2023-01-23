package com.reactivespring.moviesservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfo {

    private String movieInfoId;

    private String movieName;

    private Date releaseDate;

    private List<String> cast;

}
