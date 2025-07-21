package equinox.front.client;

import equinox.front.model.dto.ExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "gateway", contextId = "exchange")
public interface ExchangeServiceClient {

    @GetMapping("/exchange/api/v1/exchange")
    List<ExchangeRateDto> getRates();
}
