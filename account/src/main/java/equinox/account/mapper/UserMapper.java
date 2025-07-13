package equinox.account.mapper;

import equinox.account.model.dto.UserDto;
import equinox.account.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface UserMapper {
    UserDto toDto(User user);
}
