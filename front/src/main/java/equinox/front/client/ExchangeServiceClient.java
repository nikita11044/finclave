package equinox.front.client;

import equinox.front.model.dto.ExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "exchange", url = "${feign.exchange}")
public interface ExchangeServiceClient {

    @GetMapping("/api/v1/exchange")
    List<ExchangeRateDto> getRates();
}
