package equinox.cash.controller;

import equinox.cash.model.dto.ApiResponseDto;
import equinox.cash.model.dto.CashOperationDto;
import equinox.cash.service.CashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cash")
@RequiredArgsConstructor
public class CashController {

    private final CashService cashService;

    @PostMapping("/{login}")
    public ApiResponseDto processCashOperation(@PathVariable("login") String login, @RequestBody CashOperationDto dto) {
        return cashService.processCashOperation(login, dto);
    }
}
