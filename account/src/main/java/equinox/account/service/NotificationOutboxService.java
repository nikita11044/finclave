package equinox.account.service;

import equinox.account.client.NotificationServiceClient;
import equinox.account.jpa.NotificationOutboxRepository;
import equinox.account.mapper.NotificationMapper;
import equinox.account.model.dto.NotificationDto;
import equinox.account.model.entity.NotificationOutboxEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationOutboxService {

    private final NotificationServiceClient notificationServiceClient;
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
        try {
            notificationOutboxRepository
                    .findAllByDeliveredFalse()
                    .forEach(n -> {

                        notificationServiceClient.createNotification(
                            notificationMapper.toDto(n)
                        );

                        n.setDelivered(true);
                        notificationOutboxRepository.save(n);
                    });
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }
}
