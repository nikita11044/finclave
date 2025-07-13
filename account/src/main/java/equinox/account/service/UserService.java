package equinox.account.service;

import equinox.account.jpa.UserRepository;
import equinox.account.model.dto.ApiResponseDto;
import equinox.account.model.dto.PasswordUpdateDto;
import equinox.account.model.dto.UserDto;
import equinox.account.model.entity.Account;
import equinox.account.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public ApiResponseDto createUser(UserDto dto) {
        var response = ApiResponseDto.builder()
                .errors(new ArrayList<>())
                .build();

        validateAge(dto.getBirthdate(), response);

        validatePasswords(dto.getPassword(), dto.getConfirmPassword(), response);

        var loginNotUnique = userRepository.findByLogin(dto.getLogin()).isPresent();
        if (loginNotUnique) {
            response.setWithError(true);
            response.getErrors().add("Login is not unique");
        }

        if (response.isWithError()) {
            return response;
        }

        var user = User.builder()
                .login(dto.getLogin())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .birthdate(dto.getBirthdate())
                .build();

        userRepository.save(user);

        return response;
    }

    @Transactional
    public ApiResponseDto updateUser(String login, UserDto dto) {
        var response = ApiResponseDto.builder()
                .errors(new ArrayList<>())
                .build();

        if (dto.getBirthdate() != null) {
            validateAge(dto.getBirthdate(), response);
        }

        if (response.isWithError()) {
            return response;
        }

        var user = userRepository.findByLogin(login)
                .map(existingUser -> {
                    if (dto.getName() != null) {
                        existingUser.setName(dto.getName());
                    }

                    if (dto.getBirthdate() != null) {
                        existingUser.setBirthdate(dto.getBirthdate());
                    }

                    return existingUser;
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid login"));

        userRepository.save(user);

        return response;
    }

    @Transactional
    public ApiResponseDto updatePassword(String login, PasswordUpdateDto dto) {
        var response = ApiResponseDto.builder()
                .errors(new ArrayList<>())
                .build();

        validatePasswords(dto.getPassword(), dto.getConfirmPassword(), response);

        if (response.isWithError()) {
            return response;
        }

        return userRepository.findByLogin(login)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    userRepository.save(user);
                    return response;
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid login"));
    }

    @Transactional(readOnly = true)
    public UserDto getUser(String login) {
        return userRepository.findByLogin(login)
                .map(user -> UserDto.builder()
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .birthdate(user.getBirthdate())
                        .name(user.getName())
                        .build())
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isNullOrBlank(String input) {
        return input == null || input.isBlank();
    }

    private void validateAge(LocalDate birthdate, ApiResponseDto response) {
        var age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < 18) {
            response.setWithError(true);
            response.getErrors().add("Invalid age");
        }
    }

    private void validatePasswords(String password, String confirmPassword, ApiResponseDto response) {
        var passwordMissing = isNullOrBlank(password) || isNullOrBlank(confirmPassword);
        if (passwordMissing) {
            response.setWithError(true);
            response.getErrors().add("Password must not be blank");
        } else {
            var passwordsMatch = password.equals(confirmPassword);
            if (!passwordsMatch) {
                response.setWithError(true);
                response.getErrors().add("Passwords do not match");
            }
        }
    }
}
