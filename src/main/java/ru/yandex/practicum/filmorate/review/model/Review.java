package ru.yandex.practicum.filmorate.review.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.user.model.User;

@Data
public class Review {
    private Long id;
    private String content;
    private Boolean isPositive;
    private User user;
    private Film film;
    private Integer useful;
}
