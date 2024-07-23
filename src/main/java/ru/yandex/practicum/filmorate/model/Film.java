package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class Film {
    private int likeFilms;
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

    public void setLikeFilm(int filmId) {
        likeFilms++;
    }

    public void deleteLikeFilm(int filmId) {
        likeFilms--;
    }
}
