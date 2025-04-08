package ru.yandex.practicum.filmorate.review.service;

import ru.yandex.practicum.filmorate.review.dto.ReviewDto;
import ru.yandex.practicum.filmorate.review.dto.ReviewRequest;
import ru.yandex.practicum.filmorate.pubService.BaseService;

import java.util.List;

public interface ReviewService extends BaseService<ReviewDto, ReviewRequest> {
    List<ReviewDto> getAll();

    List<ReviewDto> getFilmReviews(Long filmId, int count);

    ReviewDto updateScore(Long id, Long userId, int score);
}
