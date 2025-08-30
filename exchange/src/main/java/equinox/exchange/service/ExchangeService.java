package equinox.exchange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import equinox.exchange.jpa.ExchangeRepository;
import equinox.exchange.mapper.ExchangeMapper;
import equinox.exchange.model.dto.ExchangeRateDto;
import equinox.exchange.model.dto.ExchangeRateUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
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
    private final ObjectMapper objectMapper;

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
    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 1_00, multiplier = 2, maxDelay = 8_000),
            retryTopicSuffix = "-retry",
            dltTopicSuffix = "-dlt",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "exchange-rate", containerFactory = "customKafkaListenerContainerFactory")
    public void updateRandomCurrency(String exchangeRate) {
        try {
            ExchangeRateUpdateDto dto = objectMapper.readValue(exchangeRate, ExchangeRateUpdateDto.class);
            exchangeRepository.findRandomByBaseFalse()
                    .ifPresent(rate -> {
                        rate.setRate(dto.getRate());
                        exchangeRepository.save(rate);
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DltHandler
    public void handleDltMessage(ConsumerRecord<?, ?> record) {
        System.out.println("Message in DLT: " + record.value());
    }
}
