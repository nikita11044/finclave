package equinox.cash.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "gateway", contextId = "blocker")
public interface BlockerServiceClient {

    @PostMapping("/blocker/api/v1/blocker/is-fraudulent")
    boolean isFraudulent();
}
