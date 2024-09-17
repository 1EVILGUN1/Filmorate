package ru.yandex.practicum.filmorate.controller.model.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorDto;
import ru.yandex.practicum.filmorate.controller.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.controller.model.rating.RatingDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private RatingDto mpa;
    private List<GenreDto> genres;
    private List<DirectorDto> directors;

    public FilmDto() {
        this.genres = new ArrayList<>();
        this.directors = new ArrayList<>();
    }
}
