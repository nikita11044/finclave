package equinox.noitification.service;

import equinox.noitification.jpa.NotificationRepository;
import equinox.noitification.model.dto.NotificationDto;
import equinox.noitification.model.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void createNotification(NotificationDto dto) {
        notificationRepository.insert(
                dto.getNotificationId(),
                dto.getLogin(),
                dto.getMessage()
        );
    }

    @Transactional(readOnly = true)
    public List<String> getAllByLogin(String login) {
        var page = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        return notificationRepository.findAllByLogin(login, page).map(Notification::getMessage).toList();
    }
}
