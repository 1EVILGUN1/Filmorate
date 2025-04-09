package filmorate.activity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import filmorate.enums.EventType;
import filmorate.enums.Operation;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class Activity {
    private Instant timestamp;
    private Long userId;
    private EventType eventType;
    private Operation operation;
    private Long eventId;
    private Long entityId;

    public Activity(Long userId, EventType eventType, Operation operation, Long entityId) {
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
        timestamp = Instant.now();

    }
}
