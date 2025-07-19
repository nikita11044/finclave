package equinox.front.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortDto {
    private String login;
    private String name;
}
