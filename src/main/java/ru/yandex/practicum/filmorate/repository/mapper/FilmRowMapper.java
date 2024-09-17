package ru.yandex.practicum.filmorate.repository.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

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
