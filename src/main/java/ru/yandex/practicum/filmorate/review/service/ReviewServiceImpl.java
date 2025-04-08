package ru.yandex.practicum.filmorate.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.review.dto.ReviewDto;
import ru.yandex.practicum.filmorate.review.dto.ReviewRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.review.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.activity.model.Activity;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.review.model.Review;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.activity.repository.ActivityRepository;
import ru.yandex.practicum.filmorate.film.repository.FilmRepository;
import ru.yandex.practicum.filmorate.review.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private static final String REVIEW_NOT_FOUND_MSG = "Отзыв с ID: {} не найден";
    private static final String INVALID_ID_MSG = "ID должен быть больше 0";

    private final ReviewRepository repository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final ActivityRepository activityRepository;

    public ReviewDto get(Long id) {
        log.info("Запрос на получение отзыва с ID: {}", id);
        return executeWithLogging(
                () -> repository.findOne(id)
                        .map(ReviewMapper::mapToReviewDto)
                        .orElseThrow(() -> new NotFoundException(formatNotFoundMessage(id))),
                "Успешно получен отзыв: {}",
                REVIEW_NOT_FOUND_MSG,
                "Ошибка при получении отзыва с ID: {}", id
        );
    }

    @Override
    public List<ReviewDto> getAll() {
        log.info("Запрос на получение всех отзывов");
        return executeWithLogging(
                () -> repository.findMany().stream()
                        .map(ReviewMapper::mapToReviewDto)
                        .collect(Collectors.toList()),
                "Успешно получено {} отзывов",
                null,
                "Ошибка при получении списка отзывов"
        );
    }

    public ReviewDto save(ReviewRequest request) {
        log.info("Запрос на создание отзыва: {}", request);
        try {
            validateIds(request);
            Review review = prepareAndSaveReview(request);
            recordActivity(review.getUser().getId(), EventType.REVIEW, Operation.ADD, review.getId());
            return ReviewMapper.mapToReviewDto(review);
        } catch (NotFoundException e) {
            log.warn("Ошибка валидации при создании отзыва: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при создании отзыва: {}", request, e);
            throw e;
        }
    }

    public ReviewDto update(ReviewRequest request) {
        log.info("Запрос на обновление отзыва: {}", request);
        try {
            validateReviewId(request.getReviewId());
            Review updatedReview = updateReviewFields(request);
            recordActivity(updatedReview.getUser().getId(), EventType.REVIEW, Operation.UPDATE, updatedReview.getId());
            return ReviewMapper.mapToReviewDto(updatedReview);
        } catch (NotFoundException e) {
            log.warn("Отзыв с ID: {} не найден для обновления", request.getReviewId());
            throw e;
        } catch (ValidationException e) {
            log.warn("Ошибка валидации при обновлении отзыва: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при обновлении отзыва: {}", request, e);
            throw e;
        }
    }

    public boolean delete(Long id) {
        log.info("Запрос на удаление отзыва с ID: {}", id);
        try {
            Optional<Review> optionalReview = repository.findOne(id);
            if (optionalReview.isPresent()) {
                Review review = optionalReview.get();
                recordActivity(review.getUser().getId(), EventType.REVIEW, Operation.REMOVE, id);
            } else {
                log.warn(REVIEW_NOT_FOUND_MSG, id);
            }
            boolean isDeleted = repository.delete(id);
            log.debug("Результат удаления отзыва с ID: {} - {}", id, isDeleted);
            return isDeleted;
        } catch (Exception e) {
            log.error("Ошибка при удалении отзыва с ID: {}", id, e);
            throw e;
        }
    }

    public List<ReviewDto> getFilmReviews(Long filmId, int count) {
        log.info("Запрос на получение отзывов для фильма ID: {} с лимитом: {}", filmId, count);
        return executeWithLogging(
                () -> repository.findFilmReviews(filmId, count).stream()
                        .map(ReviewMapper::mapToReviewDto)
                        .collect(Collectors.toList()),
                "Успешно получено {} отзывов для фильма ID: {}",
                null,
                "Ошибка при получении отзывов для фильма ID: {}, лимит: {}", filmId, count
        );
    }

    public ReviewDto updateScore(Long id, Long userId, int score) {
        log.info("Запрос на обновление оценки отзыва ID: {} от пользователя ID: {} со значением: {}", id, userId, score);
        try {
            repository.updateScore(id, userId, score);
            return getReviewOrThrow(id);
        } catch (NotFoundException e) {
            log.warn(REVIEW_NOT_FOUND_MSG, id);
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при обновлении оценки отзыва ID: {} от пользователя ID: {}", id, userId, e);
            throw e;
        }
    }

    // Вспомогательные методы
    private <T> T executeWithLogging(SupplierWithException<T> supplier, String successMsg, String warnMsg, String errorMsg, Object... args) {
        try {
            T result = supplier.get();
            log.debug(successMsg, getLogArgs(result, args));
            return result;
        } catch (NotFoundException e) {
            if (warnMsg != null) log.warn(warnMsg, args);
            throw e;
        } catch (Exception e) {
            log.error(errorMsg, args, e);
            throw new RuntimeException(e);
        }
    }

    private void validateIds(ReviewRequest request) {
        if (request.getUserId() < 1 || request.getFilmId() < 1) {
            log.warn("Некорректные ID: userId = {}, filmId = {}", request.getUserId(), request.getFilmId());
            throw new NotFoundException(INVALID_ID_MSG);
        }
    }

    private void validateReviewId(Long reviewId) {
        if (reviewId == null) {
            log.warn("Отсутствует ID отзыва в запросе");
            throw new ValidationException("ID", "Должен быть указан ID");
        }
    }

    private Review prepareAndSaveReview(ReviewRequest request) {
        Review review = ReviewMapper.mapToReview(request);
        setUserAndFilm(review, request);
        Review savedReview = repository.save(review);
        log.debug("Отзыв успешно сохранен с ID: {}", savedReview.getId());
        return savedReview;
    }

    private Review updateReviewFields(ReviewRequest request) {
        Review review = repository.findOne(request.getReviewId())
                .map(r -> ReviewMapper.updateReviewFields(r, request))
                .orElseThrow(() -> new NotFoundException(formatNotFoundMessage(request.getReviewId())));
        Review updatedReview = repository.update(review);
        log.debug("Отзыв успешно обновлен с ID: {}", updatedReview.getId());
        return updatedReview;
    }

    private ReviewDto getReviewOrThrow(Long id) {
        ReviewDto review = repository.findOne(id)
                .map(ReviewMapper::mapToReviewDto)
                .orElseThrow(() -> new NotFoundException(formatNotFoundMessage(id)));
        log.debug("Оценка успешно обновлена для отзыва ID: {}", id);
        return review;
    }

    private void recordActivity(Long userId, EventType eventType, Operation operation, Long reviewId) {
        Activity activity = new Activity(userId, eventType, operation, reviewId);
        activityRepository.save(activity);
        log.debug("Событие {} отзыва записано для пользователя ID: {}", operation.name().toLowerCase(), userId);
    }

    private void setUserAndFilm(Review review, ReviewRequest request) {
        log.debug("Установка пользователя и фильма для отзыва с userId: {} и filmId: {}", request.getUserId(), request.getFilmId());
        try {
            User user = fetchUser(request.getUserId());
            Film film = fetchFilm(request.getFilmId());
            review.setUser(user);
            review.setFilm(film);
            log.debug("Успешно установлены пользователь ID: {} и фильм ID: {}", user.getId(), film.getId());
        } catch (NotFoundException e) {
            log.warn("Ошибка при установке данных: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Неожиданная ошибка при установке пользователя и фильма для отзыва", e);
            throw e;
        }
    }

    private User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
    }

    private Film fetchFilm(Long filmId) {
        return filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + filmId + " не найден"));
    }

    private String formatNotFoundMessage(Long id) {
        return "Отзыв с ID = " + id + " не найден";
    }

    private Object[] getLogArgs(Object result, Object... args) {
        if (result instanceof List) return new Object[]{((List<?>) result).size(), args};
        if (result instanceof Boolean) return new Object[]{args[0], result};
        return new Object[]{result};
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}