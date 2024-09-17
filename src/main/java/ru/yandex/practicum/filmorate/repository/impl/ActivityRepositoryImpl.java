package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Activity;
import ru.yandex.practicum.filmorate.repository.ActivityRepository;
import ru.yandex.practicum.filmorate.repository.BaseRepository;

import java.util.List;

@Repository
public class ActivityRepositoryImpl extends BaseRepository<Activity> implements ActivityRepository {
    private static final String GET_USER_FEED_QUERY = """
            SELECT "event_id", "user_id", "operation", "event_type", "timestamp", "entity_id" FROM "users_feed"
            WHERE "user_id" = ?;
            """;
    private static final String INSERT_QUERY = """
            INSERT INTO "users_feed" ("user_id", "operation", "event_type", "timestamp", "entity_id")
            VALUES(?,?,?,?,?);
            """;

    public ActivityRepositoryImpl(JdbcTemplate jdbc, RowMapper<Activity> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Activity> getUserFeed(Long userId) {
        return findMany(GET_USER_FEED_QUERY, userId);
    }

    @Override
    public void save(Activity activity) {
        insert(INSERT_QUERY,
                activity.getUserId(),
                activity.getOperation().toString(),
                activity.getEventType().toString(),
                activity.getTimestamp(),
                activity.getEntityId()
        );
    }
}
