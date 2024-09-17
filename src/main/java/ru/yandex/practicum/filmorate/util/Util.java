package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.repository.Repository;

public class Util {

    public static void checkId(Repository repository, Long... ids) {
        for (Long id : ids) {
            if (repository.findById(id).isEmpty()) {
                throw new NotFoundException("Объекта с ID " + id + " не существует");
            }
        }
    }
}
