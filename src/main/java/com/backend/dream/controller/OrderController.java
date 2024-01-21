package com.backend.dream.controller;

import com.backend.dream.dto.OrderDTO;
import com.backend.dream.dto.OrderDetailDTO;
import com.backend.dream.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/orders")
    public String history(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 10;

        String username = request.getRemoteUser();
        Page<OrderDTO> orderPage = orderService.listOrderByUsername(username, PageRequest.of(page, pageSize));
        model.addAttribute("listOrder", orderPage.getContent());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "/user/order/history";
    }

    @GetMapping("/admin/order")
    public String getOrderManagement() {
        return "/admin/home/order";
    }
}
