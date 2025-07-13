package equinox.front.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private Long id;
    private CurrencyDto currency;
    private BigDecimal balance;
}
