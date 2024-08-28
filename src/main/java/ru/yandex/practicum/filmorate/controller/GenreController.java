package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        log.info("GET Запрос на получение списка жанров");
        Collection<Genre> genres = genreService.getAllGenres();
        log.info("GET Запрос на получение списка жанров выполнен {}", genres);
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@NotNull @PathVariable int id) {
        log.info("GET Запрос на получение жанра по id {}", id);
        Genre genre = genreService.getGenreById(id);
        log.info("GET Запрос на получение жанра выполнен {}", genre);
        return genre;
    }
}
