package com.backend.dream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/admin")
    public String dashboard() {
        return "/admin/home/index";
    }

    @RequestMapping("/admin/product")
    public String product() {
        return "/admin/home/product";
    }

    @RequestMapping("/admin/authority")
    public String authority() {
        return "/admin/home/authority";
    }

    @RequestMapping("/admin/size")
    public String size() {
        return "/admin/home/size";
    }

    @RequestMapping("/admin/staff")
    public String staff() {
        return "/admin/home/staff";
    }

    @RequestMapping("/admin/category")
    public String Cates() {
        return "/admin/home/category";
    }

    @RequestMapping("/admin/discount")
    public String discount() {
        return "/admin/home/discount";
    }

    @RequestMapping("/admin/voucher")
    public String voucher() {
        return "/admin/home/voucher";
    }

    @RequestMapping("/admin/blockaccount")
    public String block() {
        return "/admin/home/blockaccount";
    }

    @RequestMapping("/admin/applydiscount")
    public String applydiscount() {
        return "/admin/home/applyDiscount";
    }
}
