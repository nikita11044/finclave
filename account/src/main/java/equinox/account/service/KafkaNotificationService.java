package equinox.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import equinox.account.model.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class KafkaNotificationService {
    private final KafkaTemplate<String, String> notificationsKafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createNotification(NotificationDto dto) {
        try {
            notificationsKafkaTemplate.send(
                    "notifications",
                    UUID.randomUUID().toString(),
                    objectMapper.writeValueAsString(dto)
            ).get();
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
