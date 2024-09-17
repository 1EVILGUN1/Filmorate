package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Activity;

import java.util.List;

public interface ActivityRepository {

    List<Activity> getUserFeed(Long userId);

    void save(Activity activity);

}
