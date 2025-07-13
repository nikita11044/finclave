package equinox.front.service;


import equinox.front.client.AccountServiceClient;
import equinox.front.model.dto.ApiResponseDto;
import equinox.front.model.dto.PasswordUpdateDto;
import equinox.front.model.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final AccountServiceClient accountServiceClient;
    private final FinclaveUserDetailsService userDetailsService;

    public UserDto getUser(String login) {
        return accountServiceClient.getUser(login);
    }

    public ApiResponseDto createUser(UserDto dto) {
        return accountServiceClient.createUser(dto);
    }

    public ApiResponseDto updateUser(String login, UserDto dto) {
        return accountServiceClient.updateUser(login, dto);
    }

    public ApiResponseDto updatePassword(String login, PasswordUpdateDto updatePasswordDto) {
        return accountServiceClient.updatePassword(login, updatePasswordDto);
    }

    public void authenticate(UserDto dto, HttpServletRequest request) {
        UserDetails principal = userDetailsService.loadUserByUsername(dto.getLogin());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
