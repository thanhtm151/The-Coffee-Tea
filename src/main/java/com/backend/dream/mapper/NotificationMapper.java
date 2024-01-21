package com.backend.dream.mapper;

import com.backend.dream.dto.NotificationDTO;
import com.backend.dream.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "account.id", target = "idAccount")
    @Mapping(source = "role.id", target = "id_role")
    NotificationDTO notificationToNotificationDTO(Notification notification);

    @Mapping(source = "idAccount", target = "account.id")
    @Mapping(source = "id_role", target = "role.id")
    Notification notificationDTOToNotification(NotificationDTO notificationDTO);
}
