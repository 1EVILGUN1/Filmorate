package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.inheritance.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.storage.dao.FilmDatabaseStorage.SIZE_GENRE;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDatabaseStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<Integer, Genre> getAllGenres() {
        Map<Integer, Genre> allGenre = new HashMap<>();
        String sqlQuery = "SELECT * FROM genre;";
        List<Genre> genreFromDb = jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
        for (Genre genre : genreFromDb) {
            allGenre.put(genre.getId(), genre);
        }
        return allGenre;
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        if (id > SIZE_GENRE) {
            throw new NotFoundException("Данного жанра не существует по id" + id);
        }
        String sqlQuery = "SELECT * FROM genre WHERE id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (genreRows.next()) {
            Genre genre = new Genre(genreRows.getInt("id"), genreRows.getString("name"));
            log.info("Найден жанр с id {}", id);
            return Optional.of(genre);
        }
        return Optional.empty();
    }

    @Override
    public List<Genre> getGenresOfFilm(long filmId) {
        String queryForFilmGenres = "SELECT FG.FILM_ID, FG.GENRE_ID, G.NAME FROM FILM_GENRE FG JOIN GENRE G ON G.ID = FG.GENRE_ID WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmGenres, this::makeGenre, filmId);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }
}
