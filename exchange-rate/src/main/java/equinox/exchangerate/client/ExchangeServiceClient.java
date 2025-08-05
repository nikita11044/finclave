package equinox.exchangerate.client;

import equinox.exchangerate.model.dto.ExchangeRateUpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "exchange", url = "${feign.exchange}")
public interface ExchangeServiceClient {

    @PostMapping("/api/v1/exchange/updateExchangeRate")
    void updateExchangeRate(@RequestBody ExchangeRateUpdateDto dto);
}
