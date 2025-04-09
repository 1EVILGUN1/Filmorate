package filmorate.genre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import filmorate.genre.dto.GenreDto;
import filmorate.genre.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto get(@PathVariable Long id) {
        log.info("Получен GET запрос на получение жанра с ID: {}", id);
        try {
            GenreDto genre = service.get(id);
            log.debug("Успешно получен жанр: {}", genre);
            return genre;
        } catch (Exception e) {
            log.error("Ошибка при получении жанра с ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreDto> getAll() {
        log.info("Получен GET запрос на получение всех жанров");
        try {
            List<GenreDto> genres = service.getAll();
            log.debug("Успешно возвращено {} жанров", genres.size());
            return genres;
        } catch (Exception e) {
            log.error("Ошибка при получении списка жанров", e);
            throw e;
        }
    }
}