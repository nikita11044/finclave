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
    void kafkaTest() throws JsonProcessingException, ExecutionException, InterruptedException {
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
        ).get();

        Thread.sleep(2000);
        Mockito.verify(notificationsService, Mockito.times(1)).create(Mockito.anyString());
    }
}
