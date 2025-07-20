package equinox.account.mapper;

import equinox.account.model.dto.NotificationDto;
import equinox.account.model.entity.NotificationOutboxEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "id", target = "notificationId")
    NotificationDto toDto(NotificationOutboxEntry entry);
}
