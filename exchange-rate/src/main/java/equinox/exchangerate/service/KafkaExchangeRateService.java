package equinox.exchangerate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import equinox.exchangerate.model.dto.ExchangeRateUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaExchangeRateService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public CompletableFuture<SendResult<String, String>> updateExchangeRateAsync(ExchangeRateUpdateDto dto) {
        try {
            String payload = objectMapper.writeValueAsString(dto);
            return kafkaTemplate.send(
                    "exchange-rate",
                    UUID.randomUUID().toString(),
                    payload
            );
        } catch (JsonProcessingException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
