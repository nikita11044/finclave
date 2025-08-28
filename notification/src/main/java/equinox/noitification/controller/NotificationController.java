package equinox.noitification.controller;

import equinox.noitification.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationsService notificationService;

    @GetMapping("/{login}")
    public List<String> getAllByLogin(@PathVariable("login") String login) {
        return notificationService.getAllByLogin(login);
    }
}
