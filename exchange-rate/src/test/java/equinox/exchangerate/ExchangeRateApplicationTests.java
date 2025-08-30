package equinox.exchangerate;

import equinox.exchangerate.model.dto.ExchangeRateUpdateDto;
import equinox.exchangerate.service.KafkaExchangeRateService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@SpringBootTest
@ActiveProfiles("test")
@Import(OAuthBaseTestConfig.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ExchangeRateApplicationTests {
	@Autowired
	private KafkaExchangeRateService kafkaExchangeRateService;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	void kafkaTest() {
		try (var consumerForTest = new DefaultKafkaConsumerFactory<>(
				KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker),
				new StringDeserializer(),
				new StringDeserializer()
		).createConsumer()) {
			consumerForTest.subscribe(List.of("exchange-rate"));

			var dto = ExchangeRateUpdateDto.builder()
					.rate(BigDecimal.valueOf(1 + (99 * new Random().nextDouble())))
					.build();

			kafkaExchangeRateService.updateExchangeRate(dto);

			ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumerForTest);

			for (ConsumerRecord<String, String> record : records) {
				Assertions.assertNotNull(record.key());
				Assertions.assertNotNull(record.value());
			}
		}
	}

}
