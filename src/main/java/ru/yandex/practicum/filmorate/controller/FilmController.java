package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();

    public FilmController(final FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") int id) {
        return filmStorage.getFilm(id);
    }

    @GetMapping()
    public List<Film> getListFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getPopularFilms(@PathVariable("count") int count) {
        if (count == 0) {
            count = 10;
        }
        return filmService.popularFilms(count);
    }

    @PostMapping
    public Film createFilmWeb(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilmWeb(@RequestBody Film film) {
        if (filmStorage.updateFilm(film) == null) {
            throw new ValidationException("PUT Запрос на изменение фильма не выполнен");
        }
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        filmService.createLike(id);
        return filmStorage.getFilm(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        filmService.deleteLike(id);
    }
}
