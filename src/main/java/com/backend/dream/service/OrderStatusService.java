package com.backend.dream.service;

import com.backend.dream.dto.OrderStatusDTO;

import java.util.List;

public interface OrderStatusService {
    List<OrderStatusDTO> getAll();
}
