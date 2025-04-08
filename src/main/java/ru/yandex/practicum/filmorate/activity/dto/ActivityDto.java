package ru.yandex.practicum.filmorate.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;


@Data
public class ActivityDto {
    private Long timestamp;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;
    private EventType eventType;
    private Operation operation;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long entityId;
}
