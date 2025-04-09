package filmorate.activity.dto;

import lombok.Data;
import filmorate.enums.EventType;
import filmorate.enums.Operation;

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
