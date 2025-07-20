package equinox.account.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationDto {
    private UUID notificationId;
    private String login;
    private String message;
}

