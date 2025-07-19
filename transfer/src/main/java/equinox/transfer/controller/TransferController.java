package equinox.transfer.controller;

import equinox.transfer.model.dto.ApiResponseDto;
import equinox.transfer.model.dto.TransferDto;
import equinox.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/{login}")
    public ApiResponseDto processTransferOperation(@PathVariable("login") String login, @RequestBody TransferDto dto) {
        return transferService.processTransferOperation(login, dto);
    }
}
