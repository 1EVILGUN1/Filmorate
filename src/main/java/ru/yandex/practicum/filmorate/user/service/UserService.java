package ru.yandex.practicum.filmorate.user.service;

import ru.yandex.practicum.filmorate.activity.dto.ActivityDto;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.user.dto.UserDto;
import ru.yandex.practicum.filmorate.user.dto.UserRequest;
import ru.yandex.practicum.filmorate.pubService.BaseService;

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