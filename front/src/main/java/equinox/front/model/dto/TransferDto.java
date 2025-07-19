package equinox.front.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDto {
    private String fromCurrency;
    private String toCurrency;
    private String toLogin;
    private BigDecimal amount;
}
