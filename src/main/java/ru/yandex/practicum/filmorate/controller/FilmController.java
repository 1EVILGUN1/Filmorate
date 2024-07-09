package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.manager.Manager;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    Manager manager = new Manager();

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") int id) {
        if (id == 0) throw new ValidationException("id is zero");
        return manager.getFilm(id);
    }

    @GetMapping()
    public List<Film> getListFilms() {
        if (manager.getFilms() == null) {
            throw new ValidationException("error. no films");
        }
        return manager.getFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        if (manager.createFilm(film) == null) {
            throw new ValidationException("error.createFilm");
        }
        return manager.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (manager.updateFilm(film) == null) {
            throw new ValidationException("error.updateFilm");
        }
        return manager.updateFilm(film);
    }
}
