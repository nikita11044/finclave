package equinox.exchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateDto {
    private Long id;
    private String title;
    private String code;
    private BigDecimal rate;
    private boolean base;
}
