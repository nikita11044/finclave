package equinox.exchange.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeRateUpdateDto {
    private BigDecimal rate;
}
