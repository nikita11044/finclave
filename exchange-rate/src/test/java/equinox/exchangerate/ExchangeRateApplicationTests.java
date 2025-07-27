package equinox.exchangerate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(OAuthBaseTestConfig.class)
class ExchangeRateApplicationTests {

	@Test
	void contextLoads() {
	}

}
