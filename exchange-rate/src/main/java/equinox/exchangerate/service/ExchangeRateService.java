package equinox.exchangerate.service;

import equinox.exchangerate.client.ExchangeServiceClient;
import equinox.exchangerate.dto.ExchangeRateUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeServiceClient exchangeServiceClient;

    @Scheduled(fixedDelay = 1000)
    public void update() {
        var dto = ExchangeRateUpdateDto.builder()
                .rate(new Random().nextInt(99) + 1)
                .build();

        exchangeServiceClient.updateExchangeRate(dto);
    }
}
