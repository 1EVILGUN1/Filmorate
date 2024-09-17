package ru.yandex.practicum.filmorate.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.model.review.ReviewDto;
import ru.yandex.practicum.filmorate.controller.model.review.ReviewRequest;
import ru.yandex.practicum.filmorate.model.Review;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewMapper {
    public static ReviewDto mapToReviewDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(review.getId());
        reviewDto.setContent(review.getContent());
        reviewDto.setIsPositive(review.getIsPositive());
        reviewDto.setUserId(review.getUser().getId());
        reviewDto.setFilmId(review.getFilm().getId());
        reviewDto.setUseful(review.getUseful());
        return reviewDto;
    }

    public static Review mapToReview(ReviewRequest request) {
        Review review = new Review();
        review.setContent(request.getContent());
        review.setIsPositive(request.getIsPositive());
        return review;
    }

    public static Review updateReviewFields(Review review, ReviewRequest request) {
        review.setContent(request.getContent());
        review.setIsPositive(request.getIsPositive());
        return review;
    }
}
