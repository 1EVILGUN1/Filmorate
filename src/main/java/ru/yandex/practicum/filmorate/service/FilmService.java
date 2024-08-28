package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.inheritance.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilm(film.getId()) == null) {
            log.warn("Данного id фильма не существует {}", film.getId());
            throw new FilmDoesNotExistException();
        }
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(long filmId) {
        filmStorage.deleteFilm(filmId);
    }

    public Film getFilm(long id) {
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            throw new NotFoundException("Данного id фильма не существует " + film.getId());
        }
        return film;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLikeFilm(long filmId, long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLikeFilm(long filmId, long userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikesFilm().size() - f1.getLikesFilm().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getRecommendations(long userId) {
        return filmStorage.getRecommendations(userId);
    }
}
