package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Validated
    public Film createFilm(@Valid Film film) {
        return filmStorage.createFilm(film);
    }

    @Validated
    public Film updateFilm(@Valid Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLikeFilm(@Positive long filmId, @Positive long userId) {
        userStorage.getUser(userId);
        filmStorage.getFilm(filmId).getLikeFilms().add(userId);
        log.debug("PUT Запрос. Пользователь с ID = {} лайкнул фильм с ID = {}", userId, filmId);
    }

    public void removeLikeFilm(@Positive long filmId, @Positive long userId) {
        userStorage.getUser(userId);
        filmStorage.getFilm(filmId).getLikeFilms().remove(userId);
        log.debug("DELETE Запрос. Пользователь с ID = {} удалил лайк у фильма с ID = {}", userId, filmId);
    }

    public List<Film> getPopularFilm(@Positive int count) {
        List<Film> popularFilms = filmStorage.getFilms().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getLikeFilms().size(),
                        film1.getLikeFilms().size()))
                .limit(count)
                .toList();
        log.trace("GET Запрос. Список популярных фильмов:\n{}", popularFilms);
        return popularFilms;
    }
}
