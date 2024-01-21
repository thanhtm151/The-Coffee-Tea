package com.backend.dream.controller;

import com.backend.dream.config.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/vnpay")
    public String getVNPayTemplate(Model model) {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int number = random.nextInt((max - min) + 1 ) + min;
        model.addAttribute("orderInfo", number);
        return "/user/checkout/vnpay";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) {

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String paymentProcess(HttpServletRequest request, Model model) throws Exception{
        int paymentStatus = vnPayService.orderReturn(request);

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


        String orderInfo = request.getParameter("vnp_OrderInfo");
        Date paymentTime = inputDateFormat.parse(request.getParameter("vnp_PayDate"));
        String formattedPaymentTime = outputDateFormat.format(paymentTime);
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", formattedPaymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "/user/checkout/vnpaysuccess" : "/user/checkout/vnpayfail";
    }
}
