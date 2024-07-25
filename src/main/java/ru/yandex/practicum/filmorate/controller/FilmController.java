package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.group.Create;
import ru.yandex.practicum.filmorate.validator.group.Default;
import ru.yandex.practicum.filmorate.validator.group.Update;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@RequestBody @Validated({Create.class, Default.class}) Film film) {
        log.info("POST Запрос на создание фильма {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated({Update.class, Default.class}) Film film) {
        log.info("PUT запрос на изменение фильма {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") @Positive long id) {
        log.info("GET Запрос на получение фильма по id {}", id);
        return filmService.getFilm(id);
    }

    @GetMapping()
    public List<Film> getListFilms() {
        log.info("GET Запрос на получение списка фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        log.info("GET Запрос на получение фильмов по популярности количество {}", count);
        return filmService.getPopularFilm(count);
    }


    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLikeFilm(@PathVariable("id") @Positive long id, @PathVariable("userId") @Positive long userId) {
        log.info("PUT Запрос на добавление лайка фильму. Фильм id {}. Пользователь id {}", id, userId);
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLikeFilm(@PathVariable("id") @Positive long id, @PathVariable("userId") @Positive long userId) {
        log.info("DELETE Запрос на удаление лайка у фильма {}. Пользователь id {}", id, userId);
        filmService.removeLikeFilm(id, userId);
    }
}
