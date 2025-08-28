package equinox.noitification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import equinox.noitification.jpa.NotificationRepository;
import equinox.noitification.model.dto.NotificationDto;
import equinox.noitification.model.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsService {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 1_00, multiplier = 2, maxDelay = 8_000),
            retryTopicSuffix = "-retry",
            dltTopicSuffix = "-dlt",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "notifications", containerFactory = "customKafkaListenerContainerFactory")
    public void create(String dto) {
        try {
            NotificationDto notificationDto = objectMapper.readValue(dto, NotificationDto.class);
            notificationRepository.insert(
                    notificationDto.getNotificationId(),
                    notificationDto.getLogin(),
                    notificationDto.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DltHandler
    public void handleDltMessage(ConsumerRecord<?, ?> record) {
        System.out.println("Message in DLT: " + record.value());
    }

    @Transactional(readOnly = true)
    public List<String> getAllByLogin(String login) {
        var page = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        return notificationRepository.findAllByLogin(login, page).map(Notification::getMessage).toList();
    }
}
