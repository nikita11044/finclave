package equinox.front.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private String login;
    private String password;
    private String confirmPassword;
    private String name;
    private LocalDate birthdate;
}
