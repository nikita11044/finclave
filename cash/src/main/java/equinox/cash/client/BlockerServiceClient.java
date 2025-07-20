package equinox.cash.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "blocker", url = "${feign.blocker}")
public interface BlockerServiceClient {

    @PostMapping("/api/v1/blocker/is-fraudulent")
    boolean isFraudulent();
}
