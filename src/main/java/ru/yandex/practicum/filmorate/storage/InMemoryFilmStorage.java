package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int counterId = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        final long id = ++counterId;
        film.setId(id);
        checkFilm(film);
        films.put(id, film);
        log.info("GET Запрос на создание фильма выполнен: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        checkFilm(film);
        Film updatedFilm = films.get(film.getId());
        if (updatedFilm != null) {
            films.put(film.getId(), film);
            log.info("PUT Запрос на изменение фильма выполнен: {}", film);
            return film;
        } else {
            return null;
        }
    }

    @Override
    public List<Film> getFilms() {
        log.info("GET Запрос выполняется на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(long id) {
        log.info("GET Запрос выполняется на получение фильма по id {}", id);
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
}
