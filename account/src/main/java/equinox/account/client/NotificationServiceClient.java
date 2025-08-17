package equinox.account.client;

import equinox.account.model.dto.NotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification")
public interface NotificationServiceClient {

    @PostMapping("/api/v1/notifications")
    void createNotification(@RequestBody NotificationDto dto);
}
