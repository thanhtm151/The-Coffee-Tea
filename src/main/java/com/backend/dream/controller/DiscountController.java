package com.backend.dream.controller;

import com.backend.dream.entity.Discount;
import com.backend.dream.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Controller
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;

    @GetMapping("/home")
    public String index(Model model) {
        Date currentDate = new Date();

        List<Discount> activeDiscounts = discountRepository.findByActiveDateBeforeAndExpiredDateAfter(currentDate, currentDate);

        model.addAttribute("activeDiscounts", activeDiscounts);

        return "/user/home/index";
    }
}

