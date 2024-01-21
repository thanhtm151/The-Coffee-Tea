package com.backend.dream.service;

import com.backend.dream.dto.OrderDetailDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.NoSuchElementException;

public interface OrderDetailService {
   List<OrderDetailDTO> getOrderDetailByID(Long id) throws NoSuchElementException;
}
