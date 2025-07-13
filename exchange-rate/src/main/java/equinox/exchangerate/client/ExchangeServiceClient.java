package equinox.exchangerate.client;

import equinox.exchangerate.dto.ExchangeRateUpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "exchange", url = "${feign.exchange}")
public interface ExchangeServiceClient {

    @PostMapping("/api/rates/updateExchangeRate")
    void updateExchangeRate(@RequestBody ExchangeRateUpdateDto dto);
}
