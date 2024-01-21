package com.backend.dream.mapper;

import com.backend.dream.dto.OrderDTO;
import com.backend.dream.dto.OrderDetailDTO;
import com.backend.dream.entity.OrderDetails;
import com.backend.dream.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "status.id", target = "status")
    @Mapping(source = "account.id", target = "id_account")
    @Mapping(source = "account.fullname", target = "fullname")
    @Mapping(source = "orders.detail", target = "orderDetailsDTO")
    @Mapping(source = "voucher.id", target = "id_voucher")
    @Mapping(source = "voucher.name", target = "name_voucher")
    OrderDTO orderToOrderDTO(Orders orders);

    @Mapping(source = "status", target = "status.id")
    @Mapping(source = "id_account", target = "account.id")
    @Mapping(source = "id_voucher", target = "voucher.id")
    Orders orderDTOToOrder(OrderDTO orderDTO);

    @Mapping(source = "status.id", target = "status")
    @Mapping(source = "account.id", target = "id_account")
    @Mapping(source = "account.fullname", target = "fullname")
    @Mapping(source = "orders.detail", target = "orderDetailsDTO")
    @Mapping(source = "voucher.id", target = "id_voucher")
    @Mapping(source = "voucher.name", target = "name_voucher")
    List<OrderDTO> listOrderToListOrderDTO(List<Orders> orders);

    List<OrderDetailDTO> map(List<OrderDetails> orderDetails);




}
