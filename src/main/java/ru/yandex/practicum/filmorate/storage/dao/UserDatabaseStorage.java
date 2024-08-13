package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inheritance.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDatabaseStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO users (email, login, birthday, name) VALUES (?, ?, ?, ?)";
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(sqlQuery, new String[]{"user_id"});
                    ps.setString(1, user.getEmail());
                    ps.setString(2, user.getLogin());
                    ps.setDate(3, Date.valueOf(user.getBirthday()));
                    ps.setString(4, user.getName());
                    return ps;
                },
                keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return getUser(user.getId());
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, birthday = ?, name = ? WHERE user_id = ?";
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getBirthday(), user.getName(),
                user.getId());
        return getUser(user.getId());
    }

    @Override
    public void deleteUser(long userId) {
        User user = getUser(userId);
        String sqlQuery = "DELETE FROM users WHERE user_id = ?;";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public Collection<User> getUsers() {
        Map<Long, User> users = new HashMap<>();
        String sqlQuery = "SELECT * FROM users;";
        List<User> usersFromDb = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        for (User user : usersFromDb) {
            users.put(user.getId(), user);
        }
        return users.values();
    }

    @Override
    public User getUser(long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRows.next()) {
            User user = User.builder()
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .id(userRows.getLong("user_id"))
                    .birthday((Objects.requireNonNull(userRows.getDate("birthday"))).toLocalDate())
                    .build();
            log.info("Найден пользователь с id {}", id);
            return user;
        }
        throw new NotFoundException("Пользователь с id {} не найден" + id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        String sqlQuery = "INSERT INTO friendship (user_first_id, user_second_id) VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        String sqlQuery = "DELETE FROM friendship WHERE user_first_id = ? AND user_second_id = ?;";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(long userId) {
        User user = getUser(userId);
        String sqlQuery = "SELECT * FROM users AS u WHERE u.user_id IN (SELECT f.user_second_id FROM friendship AS f WHERE f.user_first_id = ?);";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public List<User> getMutualFriends(long userId, long otherUserId) {
        String sqlQuery = "SELECT * FROM users AS u WHERE u.user_id IN (SELECT f.user_second_id FROM friendship AS f WHERE f.user_first_id = ? INTERSECT SELECT f.user_second_id FROM friendship AS f WHERE f.user_first_id = ?);";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherUserId);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {

        return User.builder()
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .id(rs.getLong("USER_ID"))
                .birthday((rs.getDate("BIRTHDAY")).toLocalDate())
                .build();
    }
}
