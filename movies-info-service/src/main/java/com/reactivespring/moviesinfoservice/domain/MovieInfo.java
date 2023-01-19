package com.reactivespring.moviesinfoservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MovieInfo {

    @Id
    private String movieInfoId;

    @NotBlank(message = "MovieInfo.movieName can't be empty")
    private String movieName;

    @NotNull(message = "MovieInfo.releaseDate can't be empty")
    private Date releaseDate;

    @NotEmpty(message = "MovieInfo.cast can't be empty")
    @Size(max = 4, message = "MovieInfo.cast max no is 4")
    private List<@NotBlank(message = "MovieInfo.cast can't be empty string") String> cast;

}
