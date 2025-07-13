package equinox.account.mapper;

import equinox.account.model.dto.AccountDto;
import equinox.account.model.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CurrencyMapper.class)
public interface AccountMapper {
    AccountDto toDto(Account account);
}
