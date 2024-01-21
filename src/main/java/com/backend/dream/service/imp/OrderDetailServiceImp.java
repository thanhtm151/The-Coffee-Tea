package com.backend.dream.service.imp;

import com.backend.dream.dto.OrderDetailDTO;
import com.backend.dream.entity.OrderDetails;
import com.backend.dream.mapper.OrderDetailMapper;
import com.backend.dream.repository.OrderDetailRepository;
import com.backend.dream.service.OrderDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@Transactional
@Service
public class OrderDetailServiceImp implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetailDTO> getOrderDetailByID(Long id) throws NoSuchElementException {
        List<OrderDetails> orderDetails = orderDetailRepository.findByID(id);
        return orderDetailMapper.listOrderDetailToListOrderDetailDTO(orderDetails);
    }
}
