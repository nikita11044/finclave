package equinox.front.controller;

import equinox.front.client.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationServiceClient notificationServiceClient;

    @GetMapping(value = "/api/notifications", produces = "application/json")
    public List<String> getAllNotifications(Authentication authentication) {
        return notificationServiceClient.getAllNotifications(authentication.getName());
    }
}
