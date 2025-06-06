package filmorate.review.repository;

import filmorate.review.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Optional<Review> findOne(Long id);

    List<Review> findMany();

    Review save(Review review);

    Review update(Review newReview);

    boolean delete(Long id);

    List<Review> findFilmReviews(Long filmId, int count);

    boolean updateScore(Long id, Long userId, int score);
}
