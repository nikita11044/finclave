package equinox.account.service.validation;

import equinox.account.model.dto.CashOperationDto;
import equinox.account.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CashValidator {
    private static final List<String> VALID_ACTIONS = List.of("GET", "PUT");

    @Transactional
    public List<String> validate(CashOperationDto dto, User user) {
        var errors = new ArrayList<String>();

        validateCurrency(dto, errors);
        validateAmount(dto, errors);
        validateAction(dto, errors);

        if (!errors.isEmpty()) return errors;

        var accountOptional = user.getAccounts().stream()
                .filter(a -> a.getCurrency().getCode().equals(dto.getCurrencyCode()))
                .findFirst();

        if (accountOptional.isEmpty()) {
            errors.add("Account with currency " + dto.getCurrencyCode() + " not found");
            return errors;
        }

        var account = accountOptional.get();

        if (isWithdrawal(dto) && insufficientFunds(account.getBalance(), dto.getAmount())) {
            errors.add("Insufficient funds");
        }

        return errors;
    }

    private void validateCurrency(CashOperationDto dto, List<String> errors) {
        if (dto.getCurrencyCode() == null || dto.getCurrencyCode().isBlank()) {
            errors.add("Currency must be provided");
        }
    }

    private void validateAmount(CashOperationDto dto, List<String> errors) {
        if (dto.getAmount() == null) {
            errors.add("Amount must not be null");
        }
    }

    private void validateAction(CashOperationDto dto, List<String> errors) {
        var action = dto.getAction();
        if (action == null || action.isBlank()) {
            errors.add("Action must be provided");
        } else if (!VALID_ACTIONS.contains(action)) {
            errors.add("Invalid account action");
        }
    }

    private boolean isWithdrawal(CashOperationDto dto) {
        return "GET".equals(dto.getAction());
    }

    private boolean insufficientFunds(BigDecimal balance, BigDecimal amount) {
        return balance.compareTo(amount) < 0;
    }
}

