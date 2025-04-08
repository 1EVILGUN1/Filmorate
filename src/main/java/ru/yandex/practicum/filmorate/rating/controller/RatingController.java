package ru.yandex.practicum.filmorate.rating.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.rating.dto.RatingDto;
import ru.yandex.practicum.filmorate.rating.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class RatingController {
    private final RatingService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingDto get(@PathVariable Long id) {
        log.info("Получен GET запрос на получение рейтинга с ID: {}", id);
        try {
            RatingDto rating = service.get(id);
            log.debug("Успешно получен рейтинг: {}", rating);
            return rating;
        } catch (Exception e) {
            log.error("Ошибка при получении рейтинга с ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RatingDto> getAll() {
        log.info("Получен GET запрос на получение всех рейтингов");
        try {
            List<RatingDto> ratings = service.getAll();
            log.debug("Успешно возвращено {} рейтингов", ratings.size());
            return ratings;
        } catch (Exception e) {
            log.error("Ошибка при получении списка рейтингов", e);
            throw e;
        }
    }
}