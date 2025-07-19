package equinox.account.service;

import equinox.account.jpa.AccountRepository;
import equinox.account.jpa.CurrencyRepository;
import equinox.account.jpa.UserRepository;
import equinox.account.mapper.UserMapper;
import equinox.account.model.dto.ApiResponseDto;
import equinox.account.model.dto.CashOperationDto;
import equinox.account.model.dto.PasswordUpdateDto;
import equinox.account.model.dto.TransferDto;
import equinox.account.model.dto.UserDto;
import equinox.account.model.entity.Account;
import equinox.account.model.entity.Currency;
import equinox.account.model.entity.User;
import equinox.account.service.validation.CashValidator;
import equinox.account.service.validation.TransferValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final UserMapper userMapper;
    private final CashValidator cashValidator;
    private final TransferValidator transferValidator;

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

        var user = new User();
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setBirthdate(dto.getBirthdate());

        userRepository.save(user);

        var rub = currencyRepository.findByCode("RUB")
                .orElseThrow(() -> new IllegalArgumentException("Currency RUB not found"));
        var usd = currencyRepository.findByCode("USD")
                .orElseThrow(() -> new IllegalArgumentException("Currency USD not found"));
        var cny = currencyRepository.findByCode("CNY")
                .orElseThrow(() -> new IllegalArgumentException("Currency CNY not found"));

        var accounts = List.of(
                buildEmptyAccount(user, rub),
                buildEmptyAccount(user, usd),
                buildEmptyAccount(user, cny)
        );

        accountRepository.saveAll(accounts);

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
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found with login: " + login));
    }

    @Transactional
    public ApiResponseDto processCashOperation(String login, CashOperationDto dto) {
        var user = userRepository.findByLoginWithAccounts(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found with login: " + login));

        var response = ApiResponseDto.builder()
                .errors(new ArrayList<>())
                .withError(false)
                .build();

        var errors = cashValidator.validate(dto, user);
        if (!errors.isEmpty()) {
            response.getErrors().addAll(errors);
            response.setWithError(true);
            return response;
        }

        var account = user.getAccounts().stream()
                .filter(a -> a.getCurrency().getCode().equals(dto.getCurrencyCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account not found"));

        switch (dto.getAction()) {
            case "GET" -> account.setBalance(account.getBalance().subtract(dto.getAmount()));
            case "PUT" -> account.setBalance(account.getBalance().add(dto.getAmount()));
            default -> throw new IllegalArgumentException("Unknown cash operation: " + dto.getAction());
        }

        userRepository.save(user);
        return response;
    }

    @Transactional
    public ApiResponseDto processTransferOperation(String login, TransferDto dto) {
        var user = userRepository.findByLoginWithAccounts(login)
                .orElseThrow(IllegalArgumentException::new);

        var accounts = user.getAccounts();

        var toUser = userRepository.findByLoginWithAccounts(login)
                .orElseThrow(IllegalArgumentException::new);

        var toUserAccounts = toUser.getAccounts();

        var response = ApiResponseDto.builder()
                .errors(new ArrayList<>())
                .withError(false)
                .build();

        var errors = transferValidator.validate(dto, user, toUser);

        if (!errors.isEmpty()) {
            response.getErrors().addAll(errors);
            response.setWithError(true);
            return response;
        }

        var accountFrom = accounts.stream()
                .filter(a -> a.getCurrency().getCode().equals(dto.getFromCurrency()))
                .findFirst()
                .orElseThrow();

        var accountTo = toUserAccounts.stream()
                .filter(a -> a.getCurrency().getCode().equals(dto.getToCurrency()))
                .findFirst()
                .orElseThrow();

        accountFrom.setBalance(accountFrom.getBalance().subtract(dto.getAmount()));
        accountTo.setBalance(accountTo.getBalance().add(dto.getConvertedAmount()));

        userRepository.save(user);
        userRepository.save(toUser);

        return response;
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

    private Account buildEmptyAccount(User user, Currency currency) {
        var account = new Account();
        account.setCurrency(currency);
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        return account;
    }
}
