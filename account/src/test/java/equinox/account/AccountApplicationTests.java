package equinox.account;

import equinox.account.model.dto.NotificationDto;
import equinox.account.service.KafkaNotificationService;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class AccountApplicationTests extends TestContainersBaseTest {
    @Autowired
    private KafkaNotificationService kafkaNotificationService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(2)
    void kafkaTest() {
        var uuid = UUID.randomUUID();

        NotificationDto dto = NotificationDto.builder()
                .login("login")
                .message("message")
                .notificationId(uuid)
                .build();

        try (var consumerForTest = new DefaultKafkaConsumerFactory<>(
                KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker),
                new StringDeserializer(),
                new StringDeserializer()
        ).createConsumer()) {
            consumerForTest.subscribe(List.of("notifications"));

            kafkaNotificationService.createNotification(dto);


            var inputMessage = KafkaTestUtils.getSingleRecord(consumerForTest, "notifications", Duration.ofSeconds(10));
            Assertions.assertNotNull(inputMessage.key());
            Assertions.assertNotNull(inputMessage.value());
        }
    }

}
