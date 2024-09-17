package ru.yandex.practicum.filmorate.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.model.activity.ActivityDto;
import ru.yandex.practicum.filmorate.model.Activity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActivityMapper {
    public static ActivityDto mapToActivityDto(Activity activity) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setEntityId(activity.getEntityId());
        activityDto.setTimestamp(activity.getTimestamp().toEpochMilli());
        activityDto.setOperation(activity.getOperation());
        activityDto.setUserId(activity.getUserId());
        activityDto.setEventId(activity.getEventId());
        activityDto.setEventType(activity.getEventType());

        return activityDto;
    }
}
