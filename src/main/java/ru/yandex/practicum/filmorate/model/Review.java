package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Review {
    private Long id;
    private String content;
    private Boolean isPositive;
    private User user;
    private Film film;
    private Integer useful;
}
