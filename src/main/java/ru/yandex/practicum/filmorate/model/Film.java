package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;


@Data
public class Film {
    private Set<Long> likeFilms;
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

}
