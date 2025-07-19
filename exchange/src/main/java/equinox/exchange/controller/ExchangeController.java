package equinox.exchange.controller;

import equinox.exchange.model.dto.ExchangeRateDto;
import equinox.exchange.model.dto.ExchangeRateUpdateDto;
import equinox.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping
    public List<ExchangeRateDto> getRates() {
        return exchangeService.getRates();
    }

    @GetMapping("/convert")
    public BigDecimal convert(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("amount") BigDecimal amount
    ) {
        return exchangeService.convert(from, to, amount);
    }

    @PostMapping("/updateExchangeRate")
    public void updateRandomCurrency(@RequestBody ExchangeRateUpdateDto dto) {
        exchangeService.updateExchangeRate(dto);
    }
}
