package com.backend.dream.controller;

import com.backend.dream.dto.ProductDTO;
import com.backend.dream.mapper.FeedBackMapper;
import com.backend.dream.service.AccountService;
import com.backend.dream.service.FeedbackService;
import com.backend.dream.service.ProductService;
import com.backend.dream.service.UploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedBackMapper feedbackMapper;


    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductService productService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UploadService uploadService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, AccountService accountService) {
        this.feedbackService = feedbackService;
        this.accountService = accountService;
    }


    @PostMapping("/feedback/{name}")
    public String postFeedback(@PathVariable String name,
                               @RequestParam String comment,
                               @RequestParam int rating,
                               @RequestParam("file") MultipartFile file) throws IOException {
        // Check if there is a logged-in user
        String remoteUser = request.getRemoteUser();
        if (remoteUser == null) {
            return "redirect:/product/" + name;
        } else {
            try {
                // Lấy productId từ tên sản phẩm và mã hóa tên sản phẩm thành UTF-8
                String decodedName = URLDecoder.decode(name, "UTF-8");
                String encodedName = URLEncoder.encode(decodedName, StandardCharsets.UTF_8);
                ProductDTO product = productService.findByNamePaged(decodedName, PageRequest.of(0, 1)).getContent().get(0);
                Long productId = product.getId();
                Long accountId = accountService.findIDByUsername(remoteUser);
                String imagePath = null;
                if (!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    Path path = Paths.get("src/main/resources/static/img/feedback/" + fileName);
                    Files.write(path, file.getBytes());

                    imagePath = fileName;
                }

                feedbackService.createFeedback(productId, accountId, comment, rating, imagePath);
                return "redirect:/product/" + encodedName;
            } catch (UnsupportedEncodingException e) {
                return "/user/home/404";
            }
        }
    }


    @GetMapping("/deleteComment/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam String name) {
        feedbackService.deleteFeedback(id);
        try {
            String decodedName = URLDecoder.decode(name, "UTF-8");
            String encodedName = URLEncoder.encode(decodedName, "UTF-8");
            return "redirect:/product/" + encodedName;
        } catch (UnsupportedEncodingException e) {
            return "/user/home/404";
        }
    }


}