package equinox.cash.service;

import equinox.cash.client.AccountServiceClient;
import equinox.cash.model.dto.ApiResponseDto;
import equinox.cash.model.dto.CashOperationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashService {
    private final AccountServiceClient accountServiceClient;

    public ApiResponseDto processCashOperation(String login, CashOperationDto dto) {
        return accountServiceClient.processCashOperation(login, dto);
    }
}
