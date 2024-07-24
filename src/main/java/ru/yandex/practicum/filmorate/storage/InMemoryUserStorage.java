package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    protected long counterId = 0;
    protected final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(getCounterId());
        users.put(user.getId(), user);
        log.info("GET Запрос на создание пользователя выполнен {} ", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        @NotNull
        Long idNewUser = user.getId();
        User oldUser = users.get(idNewUser);
        if (oldUser == null) {
            throw new NotFoundException("Пользователь с ID = " + idNewUser + " не найден");
        } else {
            users.put(idNewUser, user);
            log.info("PUT Запрос пользователь успешно изменен {}", user);
            return user;
        }
    }

    @Override
    public List<User> getUsers() {
        log.info("GET Запрос выполняется на получение списка пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("Пользователь с ID = " + id + " не найден");
        }
        log.info("GET Запрос выполняется на получение пользователя по id: {}", id);
        return users.get(id);
    }

    private long getCounterId() {
        return ++counterId;
    }
}
