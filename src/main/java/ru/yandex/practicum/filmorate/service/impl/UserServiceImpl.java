package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.mapper.ActivityMapper;
import ru.yandex.practicum.filmorate.controller.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.controller.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.controller.model.activity.ActivityDto;
import ru.yandex.practicum.filmorate.controller.model.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.model.user.UserRequest;
import ru.yandex.practicum.filmorate.controller.model.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Activity;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.repository.ActivityRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final ActivityRepository activityRepository;

    public UserDto get(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + id + " не найден"));
    }

    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto save(UserRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            request.setName(request.getLogin());
        }
        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UserRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID", "Должен быть указан ID");
        }
        User updatedUser = userRepository.findById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + request.getId() + " не найден"));
        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public boolean delete(Long id) {
        return userRepository.delete(id);
    }

    public List<UserDto> getFriends(Long id) {
        Util.checkId(userRepository, id);
        return userRepository.getFriends(id).stream()
                .map(UserMapper::mapToUserDto)
                .sorted(Comparator.comparingLong(UserDto::getId))
                .collect(Collectors.toList());
    }

    public Set<UserDto> getCommonFriends(Long id, Long otherId) {
        Util.checkId(userRepository, id, otherId);
        return userRepository.getCommonFriends(id, otherId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    public boolean addFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя добавить самого себя в друзья");
        }

        Util.checkId(userRepository, id, otherId);


        if (userRepository.isFriendRequest(id, otherId)) {
            return userRepository.acceptRequest(id, otherId);
        }

        Activity activity = new Activity(id, EventType.FRIEND, Operation.ADD, otherId);
        activityRepository.save(activity);

        return userRepository.addFriend(id, otherId);
    }

    public boolean deleteFriend(Long id, Long otherId) {
        if (id.equals(otherId)) {
            throw new RuntimeException("Нельзя удалить самого себя из друзей");
        }
        Util.checkId(userRepository, id, otherId);



        if (userRepository.isFriend(id, otherId)) {
            return userRepository.removeRequest(id, otherId);
        }

        Activity activity = new Activity(id, EventType.FRIEND, Operation.REMOVE, otherId);
        activityRepository.save(activity);

        return userRepository.deleteFriend(id, otherId);
    }

    public List<FilmDto> getRecommendations(Long userId) {
        List<Long> bestRepetitionUserIds = userRepository.getBestRepetitionUserIds(userId);

        if (bestRepetitionUserIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<FilmDto> recommendationsFilms = bestRepetitionUserIds.stream()
                .flatMap(userIdBestRep -> filmRepository.getRecommendations(userId, userIdBestRep).stream())
                .map(FilmMapper::mapToFilmDto)
                .toList();

        if (!recommendationsFilms.isEmpty()) {
            recommendationsFilms.stream()
                    .filter(filmDto -> !genreRepository.getForFilm(filmDto.getId()).isEmpty())
                    .forEach(filmDto -> filmDto.setGenres(
                                    genreRepository.getForFilm(filmDto.getId()).stream()
                                            .map(GenreMapper::mapToGenreDto)
                                            .toList()
                            )
                    );
        }
        return recommendationsFilms;
    }

    @Override
    public List<ActivityDto> getUserFeed(Long id) {

        Util.checkId(userRepository, id);

        return activityRepository.getUserFeed(id)
                .stream()
                .map(ActivityMapper::mapToActivityDto)
                .collect(Collectors.toList());
    }
}
