package equinox.front.client;

import equinox.front.model.dto.CashOperationDto;
import equinox.front.model.dto.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gateway", contextId = "cash")
public interface CashServiceClient {

    @PostMapping("/cash/api/v1/cash/{login}")
    ApiResponseDto processCashOperation(@PathVariable("login") String login, @RequestBody CashOperationDto dto);
}
