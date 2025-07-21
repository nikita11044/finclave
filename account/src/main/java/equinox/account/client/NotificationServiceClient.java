package equinox.account.client;

import equinox.account.model.dto.NotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gateway", contextId = "notification")
public interface NotificationServiceClient {

    @PostMapping("/notification/api/v1/notifications")
    void createNotification(@RequestBody NotificationDto dto);
}
