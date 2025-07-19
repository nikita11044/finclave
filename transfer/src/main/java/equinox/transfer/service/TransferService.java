package equinox.transfer.service;

import equinox.transfer.client.AccountServiceClient;
import equinox.transfer.client.ExchangeServiceClient;
import equinox.transfer.model.dto.ApiResponseDto;
import equinox.transfer.model.dto.TransferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountServiceClient accountServiceClient;
    private final ExchangeServiceClient exchangeServiceClient;

    public ApiResponseDto processTransferOperation(String login, TransferDto dto) {
        var converted = exchangeServiceClient.convert(dto.getFromCurrency(), dto.getToCurrency(), dto.getAmount());
        dto.setConvertedAmount(converted);
        return accountServiceClient.processTransferOperation(login, dto);
    }
}
