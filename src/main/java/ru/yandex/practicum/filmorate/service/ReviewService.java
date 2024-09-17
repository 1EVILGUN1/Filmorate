package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.review.ReviewDto;
import ru.yandex.practicum.filmorate.controller.model.review.ReviewRequest;

import java.util.List;

public interface ReviewService extends BaseService<ReviewDto, ReviewRequest> {
    List<ReviewDto> getAll();

    List<ReviewDto> getFilmReviews(Long filmId, int count);

    ReviewDto updateScore(Long id, Long userId, int score);
}
