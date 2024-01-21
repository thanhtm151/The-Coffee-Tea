package com.backend.dream.service;

import com.backend.dream.dto.OrderDTO;
import com.backend.dream.entity.Orders;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

public interface OrderService {
    Orders create(JsonNode orderData) throws NoSuchElementException, NullPointerException, ParseException;

    Page<OrderDTO> listOrderByUsername(String username, Pageable pageable) throws NoSuchElementException;

    List<OrderDTO> getListOrder() throws ClassNotFoundException;

    List<OrderDTO> getListOrderConfirm() throws ClassNotFoundException;

    List<OrderDTO> getListOrderIsShipping() throws ClassNotFoundException;

    List<OrderDTO> getListOrderSuccess() throws ClassNotFoundException;

    List<OrderDTO> getListOrderCancel() throws ClassNotFoundException;

    OrderDTO updateOrder(OrderDTO orderDTO) throws ClassNotFoundException, NoSuchElementException;

    List<OrderDTO> searchOrders(String username, Long statusID);

    ByteArrayInputStream getdataOrder() throws IOException;
}
