package filmorate.activity.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import filmorate.activity.dto.ActivityDto;
import filmorate.activity.model.Activity;

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
