package equinox.front.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "gateway", contextId = "notification")
public interface NotificationServiceClient {

    @GetMapping("/notification/api/v1/notifications/{login}")
    List<String> getAllNotifications(@PathVariable("login") String login);
}
