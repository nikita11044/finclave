package equinox.account.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDto {
    private String login;
    private String password;
    private String confirmPassword;
    private String name;
    private LocalDate birthdate;
    private List<AccountDto> accounts;
}
