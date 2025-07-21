package equinox.front.client;

import equinox.front.model.dto.ApiResponseDto;
import equinox.front.model.dto.TransferDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gateway", contextId = "transfer")
public interface TransferServiceClient {
    @PostMapping("/transfer/api/v1/transfer/{login}")
    ApiResponseDto processTransferOperation(@PathVariable("login") String login, @RequestBody TransferDto dto);
}
