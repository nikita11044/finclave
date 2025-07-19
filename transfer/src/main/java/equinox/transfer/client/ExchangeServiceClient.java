package equinox.transfer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "exchange", url = "${feign.exchange}")
public interface ExchangeServiceClient {
    @GetMapping("/api/v1/exchange/convert")
    BigDecimal convert(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("amount") BigDecimal amount
    );
}
