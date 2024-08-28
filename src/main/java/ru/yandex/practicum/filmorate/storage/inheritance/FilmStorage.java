package ru.yandex.practicum.filmorate.storage.inheritance;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(long id);

    Collection<Film> getFilms();

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    void deleteFilm(long filmId);

    List<Film> getRecommendations(long count);
}
