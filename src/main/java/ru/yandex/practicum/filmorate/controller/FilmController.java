package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
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

import java.util.Collection;
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
        Film createFilm = filmService.createFilm(film);
        log.info("POST Запрос на создание фильма выполнен {}", createFilm);
        return createFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated({Update.class, Default.class}) Film film) {
        log.info("PUT Запрос на изменение фильма {}", film);
        Film updateFilm = filmService.updateFilm(film);
        log.info("PUT Запрос на изменение фильма выполнен {}", updateFilm);
        return updateFilm;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") @Positive @NotNull long id) {
        log.info("GET Запрос на получение фильма по id {}", id);
        Film film = filmService.getFilm(id);
        log.info("GET Запрос на получение фильма выполнен {}", film);
        return film;
    }

    @GetMapping()
    public Collection<Film> getListFilms() {
        log.info("GET Запрос на получение списка фильмов");
        Collection<Film> films = filmService.getFilms();
        log.info("GET Запрос на получение списка фильмов выполнен {}", films);
        return films;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET Запрос на получение фильмов по популярности количество {}", count);
        List<Film> popularFilms = filmService.getPopularFilm(count);
        log.info("GET Запрос на получение фильмов по популярности выполнен {}", popularFilms);
        return popularFilms;
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

    @DeleteMapping(value = "/{filmId}")
    public void deleteFilm(@NotNull @PathVariable long filmId) {
        log.info("DELETE Запрос на удаление фильма по id {}", filmId);
        filmService.deleteFilm(filmId);
    }
}
