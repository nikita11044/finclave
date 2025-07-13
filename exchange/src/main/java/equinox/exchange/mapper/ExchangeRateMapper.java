package equinox.exchange.mapper;


import equinox.exchange.model.dto.ExchangeRateDto;
import equinox.exchange.model.entity.ExchangeRate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {
    ExchangeRateDto toDto(ExchangeRate exchangeRate);
}
