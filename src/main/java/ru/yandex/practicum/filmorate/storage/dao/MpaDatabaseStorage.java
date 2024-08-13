package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.inheritance.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDatabaseStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final int SIZE_MPA = 5;

    @Override
    public Map<Integer, Mpa> getAllMpa() {
        Map<Integer, Mpa> allMpa = new HashMap<>();
        String sqlQuery = "SELECT * FROM rating;";
        List<Mpa> mpaFromDb = jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
        for (Mpa mpa : mpaFromDb) {
            allMpa.put(mpa.getId(), mpa);
        }
        return allMpa;
    }

    @Override
    public Optional<Mpa> getMpa(Integer id) {
        if(id>SIZE_MPA){
            throw new NotFoundException("Данного рейтинга не существует по id" + id);
        }
        String sqlQuery = "SELECT * FROM rating WHERE rating_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(mpaRows.getInt("rating_id"), mpaRows.getString("rating_name"));
            log.info("Найден рейтинг с id {}", id);
            return Optional.of(mpa);
        }
        return Optional.empty();
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("rating_id"), rs.getString("rating_name"));
    }
}