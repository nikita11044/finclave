package equinox.account.mapper;

import equinox.account.model.dto.CurrencyDto;
import equinox.account.model.entity.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto toDto(Currency currency);
}
