package equinox.account.service;

import equinox.account.jpa.NotificationOutboxRepository;
import equinox.account.mapper.NotificationMapper;
import equinox.account.model.entity.NotificationOutboxEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationOutboxService {

    private final KafkaNotificationService kafkaNotificationService;
    private final TransactionTemplate transactionTemplate;
    private final NotificationOutboxRepository notificationOutboxRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public void scheduleNotification(String login, String message) {
        notificationOutboxRepository.save(
                NotificationOutboxEntry.builder()
                        .login(login)
                        .message(message)
                        .build()
        );
    }

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void sendScheduledNotifications() {
        notificationOutboxRepository
                .findAllByDeliveredFalse()
                .forEach(n -> {
                    var dto = notificationMapper.toDto(n);

                    kafkaNotificationService.createNotificationAsync(dto)
                    .whenComplete((res, ex) -> {
                        if (ex == null) {
                            transactionTemplate.execute(status -> {
                                notificationOutboxRepository.markDelivered(n.getId());
                                return null;
                            });
                        } else {
                            log.error("Kafka send failed for outbox, id={}", n.getId(), ex);
                        }
                    });
        });
    }
}
