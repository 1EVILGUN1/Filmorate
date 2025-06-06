package filmorate.film.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import filmorate.director.model.Director;
import filmorate.exception.NotFoundException;
import filmorate.film.model.Film;
import filmorate.genre.model.Genre;
import filmorate.rating.model.Rating;
import filmorate.genre.repository.GenreRepository;
import filmorate.rating.repository.RatingRepository;
import filmorate.director.repository.DirectorRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmRowMapper implements RowMapper<Film> {
    DirectorRepository directorRepository;
    GenreRepository genreRepository;
    RatingRepository ratingRepository;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        Long ratingId = rs.getLong("rating_id");

        Rating mpa = ratingRepository.get(ratingId)
                        .orElseThrow(() -> new NotFoundException("Рейтинга с ID = " + ratingId + " не существует"));
        film.setMpa(mpa);

        Set<Genre> genres = genreRepository.getForFilm(film.getId());
        film.setGenres(genres);

        Set<Director> directors = directorRepository.getForFilm(film.getId());
        film.setDirectors(directors);
        return film;
    }
}
