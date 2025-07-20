package equinox.cash.service;

import equinox.cash.client.AccountServiceClient;
import equinox.cash.client.BlockerServiceClient;
import equinox.cash.model.dto.ApiResponseDto;
import equinox.cash.model.dto.CashOperationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CashService {
    private final AccountServiceClient accountServiceClient;
    private final BlockerServiceClient blockerServiceClient;

    public ApiResponseDto processCashOperation(String login, CashOperationDto dto) {
        if (!blockerServiceClient.isFraudulent()) {
            return accountServiceClient.processCashOperation(login, dto);
        } else {
            return ApiResponseDto.builder()
                    .withError(true)
                    .errors(List.of("Cash operation is possibly fraudulent"))
                    .build();

        }
    }
}
