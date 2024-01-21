package com.backend.dream.controller;

import com.backend.dream.config.EmailService;
import com.backend.dream.dto.AccountDTO;
import com.backend.dream.service.AccountService;
import com.backend.dream.config.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;

@Controller
public class RegisterController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String showRegistrationForm(AccountDTO accountDTO) {
        return "/user/security/register";
    }

    @PostMapping("/register")
    public String registerAccount(@RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @Valid AccountDTO accountDTO, BindingResult bindingResult, Model model) throws MessagingException {
        String username = accountDTO.getUsername();
        String email = accountDTO.getEmail();
        if (accountService.isUsernameExists(username)) {
            model.addAttribute("message", "Username valid");
            return "/user/security/register";
        }
        // if (accountService.isEmailExists(email)) {
        // model.addAttribute("message", "Email valid");
        // return "/user/security/register";
        // }
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Please review registration information");
            model.addAttribute("accountDTO", accountDTO);
            return "/user/security/register";
        }
        if (password.equals(confirmPassword)) {
            accountService.registerAccount(accountDTO);
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Wrong Password");
            return "/user/security/register";
        }
    }
}
