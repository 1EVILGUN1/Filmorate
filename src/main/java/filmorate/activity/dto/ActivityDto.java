package filmorate.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import filmorate.enums.EventType;
import filmorate.enums.Operation;


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
