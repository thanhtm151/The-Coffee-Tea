package com.backend.dream.controller;

import com.backend.dream.entity.Account;
import com.backend.dream.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String getLoginForm() {
        return "/user/security/login";
    }

    @PostMapping("/login/process")
    public String loginProcess(Model model, @RequestParam("username") String username,
            @RequestParam("password") String password) {
        Optional<Account> account = accountService.findByUsername(username);
        if (account.isEmpty())
            return "redirect:/login/error";
        if (account.isPresent() && passwordEncoder.matches(password, account.get().getPassword())
                && username.equalsIgnoreCase(account.get().getUsername()))
            return "redirect:/login/success";
        return "redirect:/login/error";
    }

    @GetMapping("/login/success")
    public String successLogin() {
        return "redirect:/home";
    }

    @GetMapping("/login/error")
    public String errorLogin(Model model) {
        model.addAttribute("message", "Username or password incorect");
        return "/user/security/login";
    }

    @GetMapping("/login/unauthorized")
    public String unauthorizedLogin(Model model) {
        model.addAttribute("message", "You don't have role ");
        return "/user/security/login";
    }

    @GetMapping("/login/logout")
    public String logout() {
        return "redirect:/home";
    }
}