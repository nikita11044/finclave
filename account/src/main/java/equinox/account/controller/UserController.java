package equinox.account.controller;

import equinox.account.model.dto.ApiResponseDto;
import equinox.account.model.dto.CashOperationDto;
import equinox.account.model.dto.PasswordUpdateDto;
import equinox.account.model.dto.TransferDto;
import equinox.account.model.dto.UserDto;
import equinox.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponseDto createUser(@RequestBody UserDto dto) {
        return userService.createUser(dto);
    }

    @PutMapping("/{login}/updateUser")
    public ApiResponseDto updateUser(
            @PathVariable String login,
            @RequestBody UserDto dto) {
        return userService.updateUser(login, dto);
    }

    @PutMapping("/{login}")
    public ApiResponseDto updatePassword(
            @PathVariable String login,
            @RequestBody PasswordUpdateDto dto
    ) {
        return userService.updatePassword(login, dto);
    }

    @GetMapping("/{login}")
    public UserDto getUser(@PathVariable("login") String login) {
        return userService.getUser(login);
    }

    @PostMapping("/cash/{login}")
    public ApiResponseDto processCashOperation(@PathVariable("login") String login, @RequestBody CashOperationDto dto) {
        return userService.processCashOperation(login, dto);
    }

    @PostMapping("/transfer/{login}")
    public ApiResponseDto processTransferOperation(@PathVariable("login") String login, @RequestBody TransferDto dto) {
        return userService.processTransferOperation(login, dto);
    }
}
