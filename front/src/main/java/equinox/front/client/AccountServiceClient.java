package equinox.front.client;

import equinox.front.model.dto.ApiResponseDto;
import equinox.front.model.dto.PasswordUpdateDto;
import equinox.front.model.dto.UserDto;
import equinox.front.model.dto.UserShortDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gateway", contextId = "account")
public interface AccountServiceClient {

    @GetMapping("/account/api/v1/users/{login}")
    UserDto getUser(@PathVariable("login") String login);

    @GetMapping("/account/api/v1/users/all/{login}")
    List<UserShortDto> getAllUsers(@PathVariable("login") String login);

    @PostMapping("/account/api/v1/users")
    ApiResponseDto createUser(@RequestBody UserDto dto);

    @PutMapping("/account/api/v1/users/{login}/updateUser")
    ApiResponseDto updateUser(@PathVariable String login, @RequestBody UserDto dto);

    @PostMapping("/account/api/v1/users/auth")
    boolean authenticate(@RequestParam("login") String login, @RequestParam("password") String password);

    @PutMapping("/account/api/v1/users/{login}")
    ApiResponseDto updatePassword(@PathVariable String login, @RequestBody PasswordUpdateDto dto);
}
