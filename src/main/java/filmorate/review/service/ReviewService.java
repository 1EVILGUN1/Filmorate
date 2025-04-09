package filmorate.review.service;

import filmorate.review.dto.ReviewDto;
import filmorate.review.dto.ReviewRequest;
import filmorate.pubService.BaseService;

import java.util.List;

public interface ReviewService extends BaseService<ReviewDto, ReviewRequest> {
    List<ReviewDto> getAll();

    List<ReviewDto> getFilmReviews(Long filmId, int count);

    ReviewDto updateScore(Long id, Long userId, int score);
}
