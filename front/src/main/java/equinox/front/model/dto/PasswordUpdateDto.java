package equinox.front.model.dto;

import lombok.Data;

@Data
public class PasswordUpdateDto {
    private String password;
    private String confirmPassword;
}
