package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger logFilm = LoggerFactory.getLogger(Film.class);
    private int counterId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    private Film createFilm(Film film) {
        final int id = ++counterId;
        film.setId(id);
        checkFilm(film);
        films.put(id, film);
        logFilm.info("GET Запрос на создание фильма выполнен: {}", film);
        return film;
    }

    private Film updateFilm(Film film) {
        checkFilm(film);
        Film updatedFilm = films.get(film.getId());
        if (updatedFilm != null) {
            films.put(film.getId(), film);
            logFilm.info("PUT Запрос на изменение фильма выполнен: {}", film);
            return film;
        } else {
            return null;
        }
    }

    private List<Film> getFilms() {
        logFilm.info("GET Запрос выполняется на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    private Film getFilm(int id) {
        logFilm.info("GET Запрос выполняется на получение фильма по id {}", id);
        return films.get(id);
    }

    private void checkFilm(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название пустое");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма больше " + 200 + " символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Дата релиза фильма раньше " + LocalDate.parse("1895-12-28"));
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть больше или равна 1 минуте");
        }
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") int id) {
        logFilm.info("GET Запрос на поиск фильма по id: {}", id);
        return getFilm(id);
    }

    @GetMapping()
    public List<Film> getListFilms() {
        logFilm.info("GET Запрос на получение списка фильмов");
        return getFilms();
    }

    @PostMapping
    public Film createFilmWeb(@RequestBody Film film) {
        logFilm.info("POST Запрос на добавление фильма: {}", film);
        return createFilm(film);
    }

    @PutMapping
    public Film updateFilmWeb(@RequestBody Film film) {
        logFilm.info("PUT Запрос на изменение фильма: {}", film);
        if (updateFilm(film) == null) {
            throw new ValidationException("PUT Запрос на изменение фильма не выполнен");
        }
        return updateFilm(film);
    }
}
