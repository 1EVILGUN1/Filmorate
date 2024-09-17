package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.model.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.model.film.FilmRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private static final String TITLE = "title";
    private static final String DIRECTOR = "director";
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> getFilms() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilm(@PathVariable Long id) {
        return filmService.get(id);
    }

    @GetMapping("/search")
    public List<FilmDto> searchFilms(@RequestParam("query") String query, @RequestParam("by") String by) {
        if (by.equals(TITLE)) {
            return filmService.getSearchFilm(query);
        } else if (by.equals(DIRECTOR)) {
            return filmService.getSearchDirector(query);
        } else if (by.equals(TITLE + ',' + DIRECTOR) || by.equals(DIRECTOR + ',' + TITLE)) {
            List<FilmDto> searchDirector = filmService.getSearchDirector(query);
            searchDirector.addAll(filmService.getSearchFilm(query));
            return searchDirector.stream().distinct().collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto createFilm(@Valid @RequestBody FilmRequest request) {
        return filmService.save(request);
    }

    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody FilmRequest request) {
        return filmService.update(request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFilm(@PathVariable Long id) {
        return filmService.delete(id);
    }

    @GetMapping("/popular")
    public List<FilmDto> getFilmsTop(@RequestParam(defaultValue = "10") int count,
                                     @RequestParam(required = false) Long genreId,
                                     @RequestParam(required = false) Integer year) {
        if (count < 1) {
            throw new ValidationException("count", "Некорректный размер выборки. Размер должен быть больше нуля");
        }
        if (year != null && (year < 1895 || year > LocalDate.now().getYear())) {
            throw new ValidationException("year", "Некорректный год. Должен быть в пределах 1895 до нашего времени");
        }
        return filmService.getTopFilms(count, genreId, year);
    }


    @GetMapping("/director/{directorId}")
    public List<FilmDto> getDirectorFilmsByYears(@RequestParam String sortBy, @PathVariable Long directorId) {
        if (sortBy.equals("year")) {
            return filmService.getDirectorsFilmsByYear(directorId);
        }
        if (sortBy.equals("likes")) {
            return filmService.getDirectorsFilmsByLikes(directorId);
        } else {
            throw new ValidationException("error", "Некорректный запрос");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean putLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/common")
    public List<FilmDto> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        if (userId < 1 || friendId < 1) {
            throw new ValidationException("id", "Некорректный ID. ID должен быть больше нуля");
        }
        return filmService.getCommonFilms(userId, friendId);
    }
}