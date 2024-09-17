package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.model.review.ReviewDto;
import ru.yandex.practicum.filmorate.controller.model.review.ReviewRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Activity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.repository.ActivityRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ReviewService;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final ActivityRepository activityRepository;

    public ReviewDto get(Long id) {
        return reviewRepository.findOne(id)
                .map(ReviewMapper::mapToReviewDto)
                .orElseThrow(() -> new NotFoundException("Отзыв с ID = " + id + " не найден"));
    }

    @Override
    public List<ReviewDto> getAll() {
        return reviewRepository.findMany().stream()
                .map(ReviewMapper::mapToReviewDto)
                .collect(Collectors.toList());
    }

    public ReviewDto save(ReviewRequest request) {
        if (request.getUserId() < 1 || request.getFilmId() < 1) {
            throw new NotFoundException("ID должен быть больше 0");
        }

        Review review = ReviewMapper.mapToReview(request);
        setUserAndFilm(review, request);
        review = reviewRepository.save(review);

        Activity activity = new Activity(request.getUserId(), EventType.REVIEW, Operation.ADD, review.getId());
        activityRepository.save(activity);

        return ReviewMapper.mapToReviewDto(review);
    }

    public ReviewDto update(ReviewRequest request) {
        if (request.getReviewId() == null) {
            throw new ValidationException("ID", "Должен быть указан ID");
        }


        Review updateReview = reviewRepository.findOne(request.getReviewId())
                .map(review -> ReviewMapper.updateReviewFields(review, request))
                .orElseThrow(() -> new NotFoundException("Отзыва с ID " + request.getReviewId() + " не найден"));
        updateReview = reviewRepository.update(updateReview);


        Activity activity = new Activity(updateReview.getUser().getId(), EventType.REVIEW, Operation.UPDATE, updateReview.getId());
        activityRepository.save(activity);

        return ReviewMapper.mapToReviewDto(updateReview);
    }

    public boolean delete(Long id) {

        Optional<Review> optionalReview = reviewRepository.findOne(id);

        if (optionalReview.isPresent()) {
            Activity activity = new Activity(optionalReview.get().getUser().getId(),
                    EventType.REVIEW, Operation.REMOVE, id);
            activityRepository.save(activity);
        }

        return reviewRepository.delete(id);
    }

    public List<ReviewDto> getFilmReviews(Long filmId, int count) {
        return reviewRepository.findFilmReviews(filmId, count).stream()
                .map(ReviewMapper::mapToReviewDto)
                .collect(Collectors.toList());
    }

    public ReviewDto updateScore(Long id, Long userId, int score) {
        reviewRepository.updateScore(id, userId, score);

        return reviewRepository.findOne(id)
                .map(ReviewMapper::mapToReviewDto)
                .orElseThrow(() -> new NotFoundException("Отзыв с ID = " + id + " не найден"));
    }

    private void setUserAndFilm(Review review, ReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + request.getUserId() + " не найден"));
        review.setUser(user);
        Film film = filmRepository.findById(request.getFilmId())
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + request.getFilmId() + " не найден"));
        review.setFilm(film);
    }
}
