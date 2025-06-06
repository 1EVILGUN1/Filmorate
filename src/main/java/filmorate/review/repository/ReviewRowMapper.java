package filmorate.review.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import filmorate.film.model.Film;
import filmorate.review.model.Review;
import filmorate.user.model.User;
import filmorate.film.repository.FilmRepository;
import filmorate.user.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewRowMapper implements RowMapper<Review> {
    UserRepository userRepository;
    FilmRepository filmRepository;

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setContent(rs.getString("content"));
        review.setIsPositive(rs.getBoolean("is_positive"));
        review.setUseful(rs.getInt("useful"));

        User user = userRepository.findById(rs.getLong("user_id"))
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        review.setUser(user);
        Film film = filmRepository.findById(rs.getLong("film_id"))
                .orElseThrow(() -> new NoSuchElementException("Фильм не найден"));
        review.setFilm(film);
        return review;
    }
}
