package equinox.account.model.dto;

import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private String currency;
    private Long balance;
}
