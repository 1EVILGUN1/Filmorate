package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.inheritance.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDatabaseStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void getLikesOfFilm(Film film) {
        String sqlQuery = "SELECT USER_ID FROM film_like WHERE FILM_ID = ?";
        Set<Integer> likes = new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Integer.class, film.getId()));
        film.getLikesFilm().addAll(likes);
    }

    private Integer mapRowToLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("USER_ID");
    }
}
