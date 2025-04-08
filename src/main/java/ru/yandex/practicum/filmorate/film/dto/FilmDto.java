package ru.yandex.practicum.filmorate.film.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.director.dto.DirectorDto;
import ru.yandex.practicum.filmorate.genre.dto.GenreDto;
import ru.yandex.practicum.filmorate.rating.dto.RatingDto;

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
