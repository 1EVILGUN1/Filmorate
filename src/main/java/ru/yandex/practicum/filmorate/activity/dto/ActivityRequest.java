package ru.yandex.practicum.filmorate.activity.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;

import java.time.Instant;

@Data
public class ActivityRequest {
    private Instant timestamp;
    private Long userId;
    private EventType eventType;
    private Operation operation;
    private Long eventId;
    private Long entityId;
}
