package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    protected int counterId = 0;
    protected final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        final long id = ++counterId;
        user.setId(id);
        checkUser(user);
        users.put(id, user);
        log.info("GET Запрос на создание пользователя выполнен {} ", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkUser(user);
        User updatedUser = users.get(user.getId());
        if (updatedUser != null) {
            users.put(updatedUser.getId(), user);
            log.info("PUT Запрос пользователь успешно изменен {}", user);
            return updatedUser;
        } else {
            throw new NotFoundException("Пользователь с ID = " + user.getId() + " не найден");
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

    private void checkUser(User user) throws ValidationException {
        if (!user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("Не верный email");
        }
        if (!(user.getLogin().replaceAll(" ", "").equals(user.getLogin()))) {
            throw new ValidationException("Не правильный логин");
        }
        if (user.getName() == null) {
            log.info("Изменение имени пользователя на его логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения указан в будущем времени");
        }
    }
}
