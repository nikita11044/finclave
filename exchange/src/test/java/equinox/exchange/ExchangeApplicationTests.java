package equinox.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import equinox.exchange.model.dto.ExchangeRateUpdateDto;
import equinox.exchange.service.ExchangeService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ExchangeApplicationTests extends TestContainersBaseTest {
	@MockitoSpyBean
	private ExchangeService exchangeService;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	void kafkaTest() throws JsonProcessingException {
		var dto = ExchangeRateUpdateDto.builder()
				.rate(BigDecimal.valueOf(1 + (99 * new Random().nextDouble())))
				.build();

		var key = UUID.randomUUID().toString();
		kafkaTemplate.send(
				"exchange-rate",
				key,
				new ObjectMapper().writeValueAsString(dto)
		);

		await()
				.atMost(10, SECONDS)
				.pollInterval(200, MILLISECONDS)
				.untilAsserted(() ->
						Mockito.verify(exchangeService, Mockito.times(1))
								.updateRandomCurrency(Mockito.anyString())
				);
	}

}
