package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class DirectorRepositoryImpl extends BaseRepository<Director> implements DirectorRepository {
    private static final String INSERT_QUERY = "INSERT INTO directors(name) VALUES (?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM directors WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM directors";
    private static final String DELETE_QUERY = "DELETE FROM directors WHERE id = ?";
    private static final String UPDATE_QUERY = " UPDATE directors SET name = ? WHERE id = ?";
    private static final String FIND_DIRECTOR_FOR_FILM =
            """
                    SELECT d."id", d."name" FROM "directors" AS d
                    JOIN "film_directors" AS fd ON d."id" = fd."director_id"
                    JOIN "films" AS f ON fd."film_id" = f."id"
                    WHERE f."id" = ?
                    """;

    public DirectorRepositoryImpl(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Director> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Director> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public Set<Director> getForFilm(Long id) {
        return new HashSet<>(jdbc.query(FIND_DIRECTOR_FOR_FILM, mapper, id));
    }

    @Override
    public Director save(Director director) {
        Long id = insert(
                INSERT_QUERY,
                director.getName()
        );
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director newDirector) {
        update(
                UPDATE_QUERY,
                newDirector.getName(),
                newDirector.getId()
        );
        return newDirector;
    }
}
