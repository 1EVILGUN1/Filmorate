package ru.yandex.practicum.filmorate.activity.repository;

import ru.yandex.practicum.filmorate.activity.model.Activity;

import java.util.List;

public interface ActivityRepository {

    List<Activity> getUserFeed(Long userId);

    void save(Activity activity);

}
