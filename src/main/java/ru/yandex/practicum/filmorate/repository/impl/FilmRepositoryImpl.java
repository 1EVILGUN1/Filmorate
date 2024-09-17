package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepositoryImpl extends BaseRepository<Film> implements FilmRepository {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, rating_id)" +
                                               "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, " +
                                               "rating_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String FIND_TOP_FILMS =
            """
                    SELECT f."id", f."name", f."description", f."release_date", f."duration", f."rating_id" FROM "films" AS f
                    LEFT JOIN "user_films_liked" AS ufl ON f."id" = ufl."film_id"
                    GROUP BY f."id"
                    ORDER BY COUNT(ufl."film_id") DESC
                    LIMIT ?
                    """;
    private static final String FIND_TOP_FILMS_BY_YEAR_AND_GENRE =
            """
                    SELECT f."id", f."name", f."description", f."release_date", f."duration", f."rating_id" FROM "films" AS f
                    LEFT JOIN "user_films_liked" ufl ON f."id" = ufl."film_id"
                    WHERE EXTRACT(YEAR FROM f."release_date") = ? AND f."id" IN
                    (SELECT "film_id"
                     FROM "film_genres" AS fg
                     WHERE fg."genre_id" = ?
                     )
                    GROUP BY f."id"
                    ORDER BY COUNT(ufl."film_id") DESC
                    LIMIT ?;
                    """;
    private static final String FIND_TOP_FILMS_BY_YEAR =
            """
                    SELECT f."id", f."name", f."description", f."release_date", f."duration", f."rating_id" FROM "films" AS f
                    LEFT JOIN "user_films_liked" ufl ON f."id" = ufl."film_id"
                    WHERE EXTRACT(YEAR FROM f."release_date") = ?
                    GROUP BY f."id"
                    ORDER BY COUNT(ufl."film_id") DESC
                    LIMIT ?;
                    """;
    private static final String FIND_TOP_FILMS_BY_GENRE =
            """
                    SELECT f."id", f."name", f."description", f."release_date", f."duration", f."rating_id" FROM "films" AS f
                    LEFT JOIN "user_films_liked" ufl ON f."id" = ufl."film_id"
                    WHERE f."id" IN
                    (SELECT "film_id" FROM "film_genres" AS fg WHERE fg."genre_id" = ? GROUP BY "film_id")
                    GROUP BY f."id"
                    ORDER BY COUNT(ufl."film_id") DESC
                    LIMIT ?;
                    """;
    private static final String FIND_LIKE =
            """
                    SELECT * FROM "user_films_liked" WHERE "film_id" = ? AND "user_id" = ?;
                    """;
    private static final String ADD_LIKE =
            """
                    INSERT INTO "user_films_liked"("film_id", "user_id") VALUES (?, ?)
                    """;
    private static final String DELETE_LIKE =
            """
                    DELETE FROM "user_films_liked" WHERE "film_id" = ? AND "user_id" = ?
                    """;
    private static final String ADD_GENRE_FOR_FILM =
            """
                    INSERT INTO "film_genres"("film_id", "genre_id") VALUES (?, ?)
                    """;
    private static final String DELETE_ALL_GENRE_FOR_FILM =
            """
                    DELETE FROM "film_genres" WHERE "film_id" = ?
                    """;
    private static final String FIND_COMMON_FILMS =
            """
                    SELECT "id", "name", "description", "release_date", "duration", "rating_id" FROM "films" f
                    LEFT JOIN "user_films_liked" ufl ON f."id" = ufl."film_id"
                    WHERE f."id" IN (
                    SELECT "film_id" FROM "user_films_liked"
                    WHERE "user_id" IN (?, ?)
                    GROUP BY "film_id"
                    HAVING COUNT(*) > 1)
                    GROUP BY "id", "name", "description", "release_date", "duration", "rating_id"
                    ORDER BY COUNT(*) DESC
                    """;
    private static final String FIND_FILMS_FOR_DIRECTOR_SORT_BY_YEAR_QUERY =
            """
                    SELECT "id", "name", "description", "release_date", "duration", "rating_id"
                    FROM "films" AS f
                    LEFT JOIN "film_directors" AS fd ON f."id" = fd."film_id"
                    WHERE fd."director_id" = ?
                    GROUP BY "id", "name", "description", "release_date", "duration", "rating_id"
                    ORDER BY f."release_date"
                    """;
    private static final String FIND_FILMS_FOR_DIRECTOR_SORT_BY_LIKES_QUERY =
            """
                    SELECT f."id", f."name", f."description", f."release_date", f."duration", f."rating_id"
                    FROM "films" AS f
                    LEFT JOIN "user_films_liked" ufl ON f."id" = ufl."film_id"
                    JOIN "film_directors" fd ON f."id" = fd."film_id"
                    WHERE fd."director_id" = ?
                    GROUP BY "id", "name", "description", "release_date", "duration", "rating_id"
                    ORDER BY COUNT(ufl."user_id") DESC ;
                    """;
    private static final String ADD_DIRECTOR_FOR_FILM =
            """
                    INSERT INTO "film_directors"("film_id", "director_id") VALUES (?, ?)
                    """;

    private static final String DELETE_ALL_DIRECTOR_FOR_FILM =
            """
                    DELETE FROM "film_directors" WHERE "film_id" = ?
                    """;

    private static final String SEARCH_FILM =
            """
                    SELECT * FROM "films" WHERE LOWER("name") LIKE CONCAT('%',?,'%');
                    """;

    private static final String SEARCH_FILM_DIRECTOR =
            """
                    SELECT f."id", f."name", f."description", f."release_date", f."duration", f."rating_id"
                    FROM "films" AS f
                    JOIN "film_directors" AS fd ON f."id" = fd."film_id"
                    JOIN "directors" AS d ON fd."director_id" = d."id"
                    WHERE LOWER(d."name") LIKE CONCAT('%',?,'%')
                    """;

    private static final String GET_RECOMMENDATIONS =
            """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id FROM films AS f
                    LEFT JOIN ratings AS r ON f.rating_id = r.id
                    RIGHT JOIN (SELECT film_id FROM user_films_liked WHERE user_id = ?
                    EXCEPT SELECT film_id FROM user_films_liked WHERE user_id = ?)
                    AS liked_films ON liked_films.film_id = f.id
                    """;

    public FilmRepositoryImpl(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        insert(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        return newFilm;
    }

    @Override
    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public boolean findLike(Long id, Long userId) {
        return jdbc.queryForRowSet(FIND_LIKE, id, userId).next();
    }

    @Override
    public boolean putLike(Long id, Long userId) {
        return jdbc.update(ADD_LIKE, id, userId) > 0;
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        return jdbc.update(DELETE_LIKE, id, userId) > 0;
    }

    @Override
    public List<Film> getTopFilms(int count, Long genreId, Integer year) {
        if (genreId == null && year == null) {
            return jdbc.query(FIND_TOP_FILMS, mapper, count);
        } else if (genreId != null && year == null) {
            return jdbc.query(FIND_TOP_FILMS_BY_GENRE, mapper, genreId, count);
        } else if (genreId == null) {
            return jdbc.query(FIND_TOP_FILMS_BY_YEAR, mapper, year, count);
        } else {
            return jdbc.query(FIND_TOP_FILMS_BY_YEAR_AND_GENRE, mapper, year, genreId, count);
        }
    }

    @Override
    public List<Film> getDirectorsFilmSortByYear(Long id) {
        return jdbc.query(FIND_FILMS_FOR_DIRECTOR_SORT_BY_YEAR_QUERY, mapper, id);
    }

    @Override
    public List<Film> getDirectorsFilmSortByLikes(Long id) {
        return jdbc.query(FIND_FILMS_FOR_DIRECTOR_SORT_BY_LIKES_QUERY, mapper, id);
    }

    @Override
    public void addGenreForFilm(Long id, Long genreId) {
        jdbc.update(ADD_GENRE_FOR_FILM, id, genreId);
    }

    @Override
    public void deleteAllGenresForFilm(Long id) {
        jdbc.update(DELETE_ALL_GENRE_FOR_FILM, id);
    }

    @Override
    public List<Film> findCommonFilms(Long userId, Long friendId) {
        return jdbc.query(FIND_COMMON_FILMS, mapper, userId, friendId);
    }

    @Override
    public List<Film> getRecommendations(long userId, long bestRepetitionUserId) {
        return findMany(GET_RECOMMENDATIONS, bestRepetitionUserId, userId);
    }

    @Override
    public void addDirectorForFilm(Long id, Long directorId) {
        jdbc.update(ADD_DIRECTOR_FOR_FILM, id, directorId);
    }

    @Override
    public void deleteAllDirectorsForFilm(Long id) {
        jdbc.update(DELETE_ALL_DIRECTOR_FOR_FILM, id);
    }

    @Override
    public List<Film> getSearchFilm(String query) {
        return jdbc.query(SEARCH_FILM, mapper, query);
    }

    @Override
    public List<Film> getSearchDirector(String query) {
        return jdbc.query(SEARCH_FILM_DIRECTOR, mapper, query);
    }
}
