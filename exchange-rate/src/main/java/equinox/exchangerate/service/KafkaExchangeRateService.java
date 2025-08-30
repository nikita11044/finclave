package equinox.exchangerate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import equinox.exchangerate.model.dto.ExchangeRateUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class KafkaExchangeRateService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void updateExchangeRate(ExchangeRateUpdateDto dto) {
        try {
            kafkaTemplate.send(
                    "exchange-rate",
                    UUID.randomUUID().toString(),
                    objectMapper.writeValueAsString(dto)
            ).get();
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
