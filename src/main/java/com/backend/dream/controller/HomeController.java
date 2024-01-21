package com.backend.dream.controller;

import com.backend.dream.dto.VoucherDTO;
import com.backend.dream.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    VoucherService voucherService;

    @RequestMapping("/home")
    public String index() {
        return "/user/home/index";
    }

    @RequestMapping("/about")
    public String about() {
        return "/user/home/about";
    }

    @RequestMapping("/cart")
    public String cart() {
        return "/user/cart/cart";
    }

    @RequestMapping("/profile")
    public String profile() {
        return "/user/infor/profile";
    }

    @RequestMapping("/updatePasswordSuccess")
    public String updatePasswordSuccess() {
        return "/user/infor/updatePassSuccess";
    }

    @RequestMapping("/changePassword")
    public String changePassword() {
        return "/user/infor/changePassword";
    }

    @RequestMapping("/voucher")
    public String voucher() {
        return "/user/voucher/voucher";
    }

    @RequestMapping("/error-page")
    public String errorPage() {
        return "/error";
    }

}
