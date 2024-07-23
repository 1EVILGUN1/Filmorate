package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;


@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    public FilmService(final InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void createLike(int idFilm) {
        filmStorage.getFilm(idFilm).setLikeFilm(idFilm);
    }

    public void deleteLike(int idFilm) {
        filmStorage.getFilm(idFilm).deleteLikeFilm(idFilm);
    }

    public List<Film> popularFilms(int count) {
        List<Film> films = new ArrayList<>();
        int maxLikesFilm = 0;
        for (Film film : filmStorage.getFilms()) {
            if (film.getLikeFilms() > maxLikesFilm) {
                maxLikesFilm = film.getLikeFilms();
            }
        }
        for (Film film : filmStorage.getFilms()) {
            if (film.getLikeFilms() == maxLikesFilm) {
                films.add(film);
                maxLikesFilm--;
                if (count >= 1) {
                    count--;
                } else {
                    break;
                }
            }
        }
        return films;
    }
}
