package com.backend.dream.rest;

import com.backend.dream.dto.OrderDetailDTO;
import com.backend.dream.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/detail")
public class OrderDetailRestController {
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/{id}")
    public List<OrderDetailDTO> getOrderDetailByID(@PathVariable("id") Long id){
        return orderDetailService.getOrderDetailByID(id);
    }
}
