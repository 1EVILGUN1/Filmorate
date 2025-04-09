package filmorate.user.service;

import filmorate.activity.dto.ActivityDto;
import filmorate.film.dto.FilmDto;
import filmorate.user.dto.UserDto;
import filmorate.user.dto.UserRequest;
import filmorate.pubService.BaseService;

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