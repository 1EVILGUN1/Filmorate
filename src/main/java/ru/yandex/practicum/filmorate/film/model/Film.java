package ru.yandex.practicum.filmorate.film.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.rating.model.Rating;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Rating mpa;
    private Set<Genre> genres;
    private Set<Director> directors;
    private Set<Long> likesSet;
}
