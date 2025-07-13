package equinox.exchangerate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ExchangeRateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateApplication.class, args);
	}

}
