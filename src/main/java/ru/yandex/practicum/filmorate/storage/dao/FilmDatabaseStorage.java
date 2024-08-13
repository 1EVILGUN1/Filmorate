package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inheritance.FilmStorage;
import ru.yandex.practicum.filmorate.storage.inheritance.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDatabaseStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    static final int SIZE_MPA = 5;
    static final int SIZE_GENRE = 6;

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) VALUES (?, ?, ?, ?, ?);";
        String queryForFilmGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            if (film.getMpa().getId() > SIZE_MPA) {
                throw new ValidationException("Данного рейтинга не существует" + film.getMpa().getId());
            }
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() > SIZE_GENRE) {
                    throw new ValidationException("Данного жанра не существует");
                }
                jdbcTemplate.update(queryForFilmGenre, film.getId(), genre.getId());
            }
        }
        return getFilm(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, RATING_ID = ?, DURATION = ? WHERE FILM_ID = ?;";
        String queryForUpdateGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getMpa().getId(), film.getDuration(), film.getId());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(queryForUpdateGenre, film.getId(), genre.getId());
            }
        }
        return getFilm(film.getId());
    }

    @Override
    public Collection<Film> getFilms() {
        Map<Long, Film> films = new HashMap<>();
        String sqlQuery = "SELECT F.*, R.RATING_NAME FROM FILM AS F JOIN RATING AS R ON F.RATING_ID = R.RATING_ID ";
        List<Film> filmsFromDb = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        for (Film film : filmsFromDb) {
            films.put(film.getId(), film);
        }
        return films.values();
    }

    @Override
    public Film getFilm(long id) {
        String sqlQuery = "SELECT F.*, R.RATING_NAME FROM FILM AS F JOIN RATING AS R ON F.RATING_ID = R.RATING_ID WHERE FILM_ID = ?;";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getLong("FILM_ID"))
                    .name(filmRows.getString("NAME"))
                    .description(filmRows.getString("DESCRIPTION"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate())
                    .duration(filmRows.getInt("DURATION"))
                    .mpa(new Mpa(filmRows.getInt("RATING_ID"), filmRows.getString("RATING_NAME")))
                    .build();
            List<Genre> genresOfFilm = getGenresOfFilm(id);
            List<Integer> likes = getLikesOfFilm(film.getId());
            for (Genre genre : genresOfFilm) {
                film.getGenres().add(genre);
            }
            for (Integer like : likes) {
                film.getLikesFilm().add(Long.valueOf(like));
            }
            log.info("Найден фильм с id {}", id);
            return film;
        }
        log.warn("Фильм с id {} не найден", id);
        return null;
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = getFilm(filmId);
        User user = userStorage.getUser(userId);
        try {
            String sqlQuery = "INSERT INTO FILM_LIKE (FILM_ID, USER_ID) VALUES (?, ?);";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (Exception e) {
            log.warn("Лайк фильму с id {} от пользователя с id {} уже существует", filmId, userId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        Film film = getFilm(filmId);
        User user = userStorage.getUser(userId);
        String sqlQuery = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteFilm(long filmId) {
        Film film = getFilm(filmId);
        String sqlQuery = "DELETE FROM FILM WHERE FILM_ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Film> getRecommendations(long count) {
        String sql = "SELECT * FROM FILM F JOIN RATING R ON F.RATING_ID = R.RATING_ID WHERE F.FILM_ID IN (SELECT FILM_ID FROM FILM_LIKE WHERE USER_ID IN (SELECT FL1.USER_ID FROM FILM_LIKE FL1 RIGHT JOIN FILM_LIKE FL2 ON FL2.FILM_ID = FL1.FILM_ID GROUP BY FL1.USER_ID, FL2.USER_ID HAVING FL1.USER_ID IS NOT NULL AND FL1.USER_ID != ? AND FL2.USER_ID = ? ORDER BY COUNT(FL1.USER_ID) DESC LIMIT 3) AND FILM_ID NOT IN (SELECT FILM_ID FROM FILM_LIKE WHERE USER_ID = ?))";

        return jdbcTemplate.query(sql, this::mapRowToFilm, count, count, count);
    }

    private List<Integer> getLikesOfFilm(long filmId) {
        String queryForFilmLikes = "SELECT USER_ID FROM FILM_LIKE WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmLikes, this::mapRowToLike, filmId);
    }

    private Integer mapRowToLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("USER_ID");
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }

    private List<Genre> getGenresOfFilm(long filmId) {
        String queryForFilmGenres = "SELECT FG.FILM_ID, FG.GENRE_ID, G.GENRE_NAME FROM FILM_GENRE FG JOIN GENRE G ON G.GENRE_ID = FG.GENRE_ID WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmGenres, this::mapRowToGenre, filmId);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        log.info("Film build start>>>>>");
        Film film = Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(new Mpa(rs.getInt("RATING_ID"), rs.getString("RATING_NAME")))
                .build();
        log.info("Film build end>>>>>");
        log.info("Film = {}", film);
        List<Genre> genresOfFilm = getGenresOfFilm(film.getId());
        List<Integer> likes = getLikesOfFilm(film.getId());
        for (Genre genre : genresOfFilm) {
            film.getGenres().add(genre);
        }
        for (Integer like : likes) {
            film.getLikesFilm().add(Long.valueOf(like));
        }
        return film;
    }
}
