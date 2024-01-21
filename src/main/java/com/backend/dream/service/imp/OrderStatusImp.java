package com.backend.dream.service.imp;

import com.backend.dream.dto.OrderStatusDTO;
import com.backend.dream.mapper.OrderStatusMapper;
import com.backend.dream.repository.OrderStatusRepository;
import com.backend.dream.service.OrderStatusService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@Service
public class OrderStatusImp implements OrderStatusService {
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    public List<OrderStatusDTO> getAll() {
        return orderStatusRepository.findAll().stream().map(orderStatusMapper::orderStatusToOrderStatusDTO).collect(Collectors.toList());
    }

}
