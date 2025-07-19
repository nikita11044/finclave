package equinox.account.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashOperationDto {
    private String currencyCode;
    private BigDecimal amount;
    private String action;
}
