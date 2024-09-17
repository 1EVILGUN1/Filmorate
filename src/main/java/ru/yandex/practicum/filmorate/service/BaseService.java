package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface BaseService<T1, T2> {
    T1 get(Long id);

    List<T1> getAll();

    T1 save(T2 request);

    T1 update(T2 request);

    boolean delete(Long id);
}
