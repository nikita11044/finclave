package equinox.account.service.validation;

import equinox.account.model.dto.TransferDto;
import equinox.account.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TransferValidator {

    public List<String> validate(TransferDto dto, User user, User toUser) {
        List<String> errors = new ArrayList<>();

        if (dto.getFromCurrency() == null || dto.getFromCurrency().isBlank()) {
            errors.add("Source currency must be specified.");
        }

        if (dto.getToCurrency() == null || dto.getToCurrency().isBlank()) {
            errors.add("Target currency must be specified.");
        }

        if (dto.getToLogin() == null || dto.getToLogin().isBlank()) {
            errors.add("Recipient login must be specified.");
        }

        if (dto.getAmount() == null) {
            errors.add("Amount must be specified.");
        } else if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Amount must be greater than zero.");
        }

        if (dto.getFromCurrency() != null && dto.getToCurrency() != null &&
                dto.getFromCurrency().equals(dto.getToCurrency()) &&
                user.getLogin().equals(dto.getToLogin())) {
            errors.add("Transfers must be made between different accounts.");
        }

        var userAccounts = user.getAccounts();
        var toUserAccounts = toUser.getAccounts();

        var fromAccountOptional = userAccounts.stream()
                .filter(a -> a.getCurrency().getCode().equals(dto.getFromCurrency()))
                .findFirst();

        if (fromAccountOptional.isEmpty()) {
            errors.add("You don't have an account with currency " + dto.getFromCurrency() + ".");
        }

        var toAccountExists = toUserAccounts.stream()
                .anyMatch(a -> a.getCurrency().getCode().equals(dto.getToCurrency()));

        if (!toAccountExists) {
            errors.add("User " + toUser.getName() + " does not have an account with currency " + dto.getToCurrency() + ".");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        var accountFrom = fromAccountOptional.get();
        var balance = accountFrom.getBalance();

        if (balance == null || balance.compareTo(dto.getAmount()) < 0) {
            errors.add("Insufficient funds.");
        }

        return errors;
    }
}
