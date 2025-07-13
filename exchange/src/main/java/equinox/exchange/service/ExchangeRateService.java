package equinox.exchange.service;

import equinox.exchange.jpa.ExchangeRateRepository;
import equinox.exchange.model.dto.ExchangeRateDto;
import equinox.exchange.model.dto.ExchangeRateUpdateDto;
import equinox.exchange.model.entity.ExchangeRate;
import equinox.exchange.mapper.ExchangeRateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    @Transactional(readOnly = true)
    public BigDecimal convert(String from, String to, Long value) {
        List<ExchangeRate> rates = exchangeRateRepository.findAll();

        ExchangeRate fromRate = rates.stream()
                .filter(r -> r.getCode().equals(from))
                .findFirst()
                .orElseThrow();

        ExchangeRate toRate = rates.stream()
                .filter(r -> r.getCode().equals(to))
                .findFirst()
                .orElseThrow();

        if (fromRate.isBase() && toRate.isBase()) {
            return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal amount = BigDecimal.valueOf(value);

        return amount
                .divide(fromRate.getRate(), 10, RoundingMode.HALF_UP)
                .multiply(toRate.getRate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public List<ExchangeRateDto> getRates() {
        return exchangeRateRepository.findAll(Sort.by("id")).stream()
                .map(exchangeRateMapper::toDto)
                .toList();
    }


    @Transactional
    public void updateExchangeRate(ExchangeRateUpdateDto dto) {
        exchangeRateRepository.findRandomByBaseFalse()
                .ifPresent(rate -> {
                    rate.setRate(dto.getRate());
                    exchangeRateRepository.save(rate);
                });
    }
}
