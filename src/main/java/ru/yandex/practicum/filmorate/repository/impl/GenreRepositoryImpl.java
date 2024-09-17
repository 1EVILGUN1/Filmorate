package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreRepositoryImpl extends BaseRepository<Genre> implements GenreRepository {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_GENRE_FOR_FILM =
            """
                    SELECT g."id", g."name" FROM "genres" AS g
                    JOIN "film_genres" AS fg
                    ON g."id" = fg."genre_id"
                    JOIN "films" AS f
                    ON fg."film_id" = f."id"
                    WHERE f."id" = ?
                    """;

    public GenreRepositoryImpl(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Set<Genre> getForFilm(Long id) {
        return new HashSet<>(jdbc.query(FIND_GENRE_FOR_FILM, mapper, id));
    }
}
