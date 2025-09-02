package equinox.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import equinox.account.model.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaNotificationService {
    private final KafkaTemplate<String, String> notificationsKafkaTemplate;
    private final ObjectMapper objectMapper;

    public CompletableFuture<SendResult<String, String>> createNotificationAsync(NotificationDto dto) {
        try {
            String payload = objectMapper.writeValueAsString(dto);
            return notificationsKafkaTemplate.send(
                    "notifications",
                    UUID.randomUUID().toString(),
                    payload
            );
        } catch (JsonProcessingException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
