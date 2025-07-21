package equinox.transfer.client;

import equinox.transfer.model.dto.ApiResponseDto;
import equinox.transfer.model.dto.TransferDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gateway", contextId = "account")
public interface AccountServiceClient {
    @PostMapping("/account/api/v1/users/transfer/{login}")
    ApiResponseDto processTransferOperation(@PathVariable("login") String login, @RequestBody TransferDto dto);
}
