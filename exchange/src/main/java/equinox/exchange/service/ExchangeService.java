package equinox.exchange.service;

import equinox.exchange.jpa.ExchangeRepository;
import equinox.exchange.mapper.ExchangeMapper;
import equinox.exchange.model.dto.ExchangeRateDto;
import equinox.exchange.model.dto.ExchangeRateUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final ExchangeMapper exchangeMapper;

    @Transactional(readOnly = true)
    public BigDecimal convert(String from, String to, BigDecimal amount) {
        var rates = exchangeRepository.findAll();

        var fromRate = rates.stream()
                .filter(r -> r.getCode().equals(from))
                .findFirst()
                .orElseThrow();

        var toRate = rates.stream()
                .filter(r -> r.getCode().equals(to))
                .findFirst()
                .orElseThrow();

        if (fromRate.isBase() && toRate.isBase()) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }

        return amount
                .divide(fromRate.getRate(), 10, RoundingMode.HALF_UP)
                .multiply(toRate.getRate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public List<ExchangeRateDto> getRates() {
        return exchangeRepository.findAll(Sort.by("id")).stream()
                .map(exchangeMapper::toDto)
                .toList();
    }


    @Transactional
    public void updateExchangeRate(ExchangeRateUpdateDto dto) {
        exchangeRepository.findRandomByBaseFalse()
                .ifPresent(rate -> {
                    rate.setRate(dto.getRate());
                    exchangeRepository.save(rate);
                });
    }
}
