package ru.yandex.practicum.filmorate.film.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.film.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.film.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private static final String TITLE = "title";
    private static final String DIRECTOR = "director";
    private final FilmService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> getFilms() {
        log.info("Получен запрос на получение всех фильмов");
        Collection<FilmDto> films = service.getAll();
        log.debug("Возвращено {} фильмов", films.size());
        return films;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilm(@PathVariable Long id) {
        log.info("Получен запрос на получение фильма с ID: {}", id);
        FilmDto film = service.get(id);
        log.debug("Найден фильм: {}", film);
        return film;
    }

    @GetMapping("/search")
    public List<FilmDto> searchFilms(@RequestParam("query") String query, @RequestParam("by") String by) {
        log.info("Получен запрос на поиск фильмов по запросу '{}' в полях: {}", query, by);

        if (by.equals(TITLE)) {
            List<FilmDto> result = service.getSearchFilm(query);
            log.debug("Найдено {} фильмов по названию", result.size());
            return result;
        } else if (by.equals(DIRECTOR)) {
            List<FilmDto> result = service.getSearchDirector(query);
            log.debug("Найдено {} фильмов по режиссеру", result.size());
            return result;
        } else if (by.equals(TITLE + ',' + DIRECTOR) || by.equals(DIRECTOR + ',' + TITLE)) {
            List<FilmDto> searchDirector = service.getSearchDirector(query);
            searchDirector.addAll(service.getSearchFilm(query));
            List<FilmDto> result = searchDirector.stream().distinct().collect(Collectors.toList());
            log.debug("Найдено {} фильмов по названию и режиссеру", result.size());
            return result;
        } else {
            log.warn("Неизвестный параметр поиска: {}", by);
            return null;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto createFilm(@Valid @RequestBody FilmRequest request) {
        log.info("Получен запрос на создание нового фильма: {}", request);
        FilmDto createdFilm = service.save(request);
        log.debug("Создан новый фильм: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody FilmRequest request) {
        log.info("Получен запрос на обновление фильма с ID: {}", request.getId());
        FilmDto updatedFilm = service.update(request);
        log.debug("Обновлен фильм: {}", updatedFilm);
        return updatedFilm;
    }

    @DeleteMapping("/{id}")
    public boolean deleteFilm(@PathVariable Long id) {
        log.info("Получен запрос на удаление фильма с ID: {}", id);
        boolean isDeleted = service.delete(id);
        log.debug("Результат удаления фильма с ID {}: {}", id, isDeleted);
        return isDeleted;
    }

    @GetMapping("/popular")
    public List<FilmDto> getFilmsTop(@RequestParam(defaultValue = "10") int count,
                                     @RequestParam(required = false) Long genreId,
                                     @RequestParam(required = false) Integer year) {
        log.info("Получен запрос на получение топ-{} фильмов, genreId: {}, year: {}", count, genreId, year);

        if (count < 1) {
            log.warn("Некорректный размер выборки: {}", count);
            throw new ValidationException("count", "Некорректный размер выборки. Размер должен быть больше нуля");
        }
        if (year != null && (year < 1895 || year > LocalDate.now().getYear())) {
            log.warn("Некорректный год: {}", year);
            throw new ValidationException("year", "Некорректный год. Должен быть в пределах 1895 до нашего времени");
        }

        List<FilmDto> topFilms = service.getTopFilms(count, genreId, year);
        log.debug("Возвращено {} фильмов в топе", topFilms.size());
        return topFilms;
    }

    @GetMapping("/director/{directorId}")
    public List<FilmDto> getDirectorFilmsByYears(@RequestParam String sortBy, @PathVariable Long directorId) {
        log.info("Получен запрос на получение фильмов режиссера {} отсортированных по {}", directorId, sortBy);

        if (sortBy.equals("year")) {
            List<FilmDto> films = service.getDirectorsFilmsByYear(directorId);
            log.debug("Найдено {} фильмов режиссера, отсортированных по году", films.size());
            return films;
        }
        if (sortBy.equals("likes")) {
            List<FilmDto> films = service.getDirectorsFilmsByLikes(directorId);
            log.debug("Найдено {} фильмов режиссера, отсортированных по лайкам", films.size());
            return films;
        } else {
            log.warn("Некорректный параметр сортировки: {}", sortBy);
            throw new ValidationException("error", "Некорректный запрос");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean putLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос на добавление лайка фильму {} от пользователя {}", id, userId);
        boolean result = service.putLike(id, userId);
        log.debug("Результат добавления лайка: {}", result);
        return result;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос на удаление лайка фильму {} от пользователя {}", id, userId);
        boolean result = service.deleteLike(id, userId);
        log.debug("Результат удаления лайка: {}", result);
        return result;
    }

    @GetMapping("/common")
    public List<FilmDto> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        log.info("Получен запрос на общие фильмы пользователей {} и {}", userId, friendId);

        if (userId < 1 || friendId < 1) {
            log.warn("Некорректные ID пользователей: userId={}, friendId={}", userId, friendId);
            throw new ValidationException("id", "Некорректный ID. ID должен быть больше нуля");
        }

        List<FilmDto> commonFilms = service.getCommonFilms(userId, friendId);
        log.debug("Найдено {} общих фильмов", commonFilms.size());
        return commonFilms;
    }
}