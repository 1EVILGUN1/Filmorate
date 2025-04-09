package filmorate.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import filmorate.review.dto.ReviewDto;
import filmorate.review.dto.ReviewRequest;
import filmorate.review.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto getReview(@PathVariable Long id) {
        log.info("Получен GET запрос на получение отзыва с ID: {}", id);
        try {
            ReviewDto review = service.get(id);
            log.debug("Успешно получен отзыв: {}", review);
            return review;
        } catch (Exception e) {
            log.error("Ошибка при получении отзыва с ID: {}", id, e);
            throw e;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@Valid @RequestBody ReviewRequest request) {
        log.info("Получен POST запрос на создание отзыва: {}", request);
        try {
            ReviewDto review = service.save(request);
            log.debug("Успешно создан отзыв с ID: {}", review.getReviewId());
            return review;
        } catch (Exception e) {
            log.error("Ошибка при создании отзыва: {}", request, e);
            throw e;
        }
    }

    @PutMapping
    public ReviewDto updateReview(@Valid @RequestBody ReviewRequest request) {
        log.info("Получен PUT запрос на обновление отзыва: {}", request);
        try {
            ReviewDto review = service.update(request);
            log.debug("Успешно обновлен отзыв с ID: {}", review.getReviewId());
            return review;
        } catch (Exception e) {
            log.error("Ошибка при обновлении отзыва: {}", request, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public boolean deleteReview(@PathVariable Long id) {
        log.info("Получен DELETE запрос на удаление отзыва с ID: {}", id);
        try {
            boolean isDeleted = service.delete(id);
            log.debug("Результат удаления отзыва с ID: {} - {}", id, isDeleted);
            return isDeleted;
        } catch (Exception e) {
            log.error("Ошибка при удалении отзыва с ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getReviews(@RequestParam(required = false) Long filmId, @RequestParam(defaultValue = "10") int count) {
        log.info("Получен GET запрос на получение отзывов для фильма ID: {} с лимитом: {}", filmId, count);
        try {
            List<ReviewDto> reviews = service.getFilmReviews(filmId, count);
            log.debug("Успешно возвращено {} отзывов", reviews.size());
            return reviews;
        } catch (Exception e) {
            log.error("Ошибка при получении отзывов для фильма ID: {}, лимит: {}", filmId, count, e);
            throw e;
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public ReviewDto putLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен PUT запрос на добавление лайка отзыву ID: {} от пользователя ID: {}", id, userId);
        try {
            ReviewDto review = service.updateScore(id, userId, 1);
            log.debug("Лайк успешно добавлен к отзыву ID: {}", id);
            return review;
        } catch (Exception e) {
            log.error("Ошибка при добавлении лайка к отзыву ID: {} от пользователя ID: {}", id, userId, e);
            throw e;
        }
    }

    @PutMapping("/{id}/dislike/{userId}")
    public ReviewDto putDislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен PUT запрос на добавление дизлайка отзыву ID: {} от пользователя ID: {}", id, userId);
        try {
            ReviewDto review = service.updateScore(id, userId, -1);
            log.debug("Дизлайк успешно добавлен к отзыву ID: {}", id);
            return review;
        } catch (Exception e) {
            log.error("Ошибка при добавлении дизлайка к отзыву ID: {} от пользователя ID: {}", id, userId, e);
            throw e;
        }
    }

    @DeleteMapping(value = {"/{id}/like/{userId}", "/{id}/dislike/{userId}"})
    public ReviewDto deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен DELETE запрос на удаление оценки отзыва ID: {} от пользователя ID: {}", id, userId);
        try {
            ReviewDto review = service.updateScore(id, userId, 0);
            log.debug("Оценка успешно удалена для отзыва ID: {}", id);
            return review;
        } catch (Exception e) {
            log.error("Ошибка при удалении оценки отзыва ID: {} от пользователя ID: {}", id, userId, e);
            throw e;
        }
    }
}