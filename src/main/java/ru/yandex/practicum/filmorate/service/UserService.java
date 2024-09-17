package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.activity.ActivityDto;
import ru.yandex.practicum.filmorate.controller.model.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.model.user.UserDto;
import ru.yandex.practicum.filmorate.controller.model.user.UserRequest;

import java.util.List;
import java.util.Set;

public interface UserService extends BaseService<UserDto, UserRequest> {
    List<UserDto> getFriends(Long id);

    Set<UserDto> getCommonFriends(Long id, Long otherId);

    boolean addFriend(Long id, Long otherId);

    boolean deleteFriend(Long id, Long otherId);

    List<FilmDto> getRecommendations(Long userId);

    List<ActivityDto> getUserFeed(Long id);
}