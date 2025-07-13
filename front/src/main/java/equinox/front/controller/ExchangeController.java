package equinox.front.controller;

import equinox.front.client.ExchangeServiceClient;
import equinox.front.model.dto.ExchangeRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exchange")
public class ExchangeController {

    private final ExchangeServiceClient exchangeServiceClient;

    @GetMapping
    public List<ExchangeRateDto> getRates() {
        return exchangeServiceClient.getRates();
    }
}
