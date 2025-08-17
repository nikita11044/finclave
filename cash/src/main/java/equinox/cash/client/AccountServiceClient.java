package equinox.cash.client;

import equinox.cash.model.dto.ApiResponseDto;
import equinox.cash.model.dto.CashOperationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account")
public interface AccountServiceClient {

    @PostMapping("/api/v1/users/cash/{login}")
    ApiResponseDto processCashOperation(@PathVariable("login") String login, @RequestBody CashOperationDto dto);
}
