package ru.yandex.practicum.filmorate.controller.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReviewDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long reviewId;
    private String content;
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful;
}
