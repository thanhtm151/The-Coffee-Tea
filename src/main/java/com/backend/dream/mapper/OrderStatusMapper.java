package com.backend.dream.mapper;

import com.backend.dream.dto.OrderStatusDTO;
import com.backend.dream.entity.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderStatusMapper {
    OrderStatusMapper INSTANCE = Mappers.getMapper(OrderStatusMapper.class);
    OrderStatusDTO orderStatusToOrderStatusDTO(OrderStatus orderStatus);
    OrderStatus orderStatusDTOToOrderStatus(OrderStatusDTO orderStatusDTO);

}
