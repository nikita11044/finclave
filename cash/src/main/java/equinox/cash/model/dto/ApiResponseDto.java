package equinox.cash.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiResponseDto {
    private boolean withError;
    private final List<String> errors;
}
