package equinox.transfer.service;

import equinox.transfer.client.AccountServiceClient;
import equinox.transfer.client.BlockerServiceClient;
import equinox.transfer.client.ExchangeServiceClient;
import equinox.transfer.model.dto.ApiResponseDto;
import equinox.transfer.model.dto.TransferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountServiceClient accountServiceClient;
    private final ExchangeServiceClient exchangeServiceClient;
    private final BlockerServiceClient blockerServiceClient;

    public ApiResponseDto processTransferOperation(String login, TransferDto dto) {
        if (!blockerServiceClient.isFraudulent()) {
            return initTransfer(login, dto);
        } else {
            return ApiResponseDto.builder()
                    .withError(true)
                    .errors(List.of("Transfer operation is possibly fraudulent"))
                    .build();

        }
    }

    private ApiResponseDto initTransfer(String login, TransferDto dto) {
        var converted = exchangeServiceClient.convert(dto.getFromCurrency(), dto.getToCurrency(), dto.getAmount());
        dto.setConvertedAmount(converted);
        return accountServiceClient.processTransferOperation(login, dto);
    }
}
