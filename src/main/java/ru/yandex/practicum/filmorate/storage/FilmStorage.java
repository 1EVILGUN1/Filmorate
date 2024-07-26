package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    public Film getFilm(long id);

    public List<Film> getFilms();
}
