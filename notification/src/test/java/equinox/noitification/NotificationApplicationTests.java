package equinox.noitification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import equinox.noitification.model.dto.NotificationDto;
import equinox.noitification.service.NotificationsService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class NotificationApplicationTests extends TestContainersBaseTest {
    @Autowired
    private KafkaTemplate<String, String> notificationsKafkaTemplate;

    @MockitoSpyBean
    private NotificationsService notificationsService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(2)
    void kafkaTest() throws JsonProcessingException {
        var uuid = UUID.randomUUID();

        NotificationDto dto = NotificationDto.builder()
                .login("login")
                .message("message")
                .notificationId(uuid)
                .build();

        notificationsKafkaTemplate.send(
                "notifications",
                UUID.randomUUID().toString(),
                new ObjectMapper().writeValueAsString(dto)
        );

        await()
                .atMost(10, SECONDS)
                .pollInterval(200, MILLISECONDS)
                .untilAsserted(() ->
                        Mockito.verify(notificationsService, Mockito.times(1))
                                .create(Mockito.anyString())
                );
    }
}
