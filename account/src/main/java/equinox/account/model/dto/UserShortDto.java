package equinox.account.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserShortDto {
    private String login;
    private String name;
}
