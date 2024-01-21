package com.test.dream;

import com.backend.dream.controller.LoginController;
import com.backend.dream.entity.Account;
import com.backend.dream.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class  LoginTest {
    @InjectMocks
    private LoginController loginController;

    @Mock
    private AccountService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLoginForm() {
        String result = loginController.getLoginForm();
        assertEquals("/user/security/login", result);
    }

    @Test
    public void testLoginSuccess() {
        String username = "kiet";
        String password = "Kietlai1!";
        Account mockAccount = new Account(username, passwordEncoder.encode(password));

        when(accountService.findByUsername(username)).thenReturn(Optional.of(mockAccount));
        when(passwordEncoder.matches(password, mockAccount.getPassword())).thenReturn(true);

        String result = loginController.loginProcess(model, username, password);
        assertEquals("redirect:/login/success", result);

        verify(accountService, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, mockAccount.getPassword());
    }

    // Empty username and password
    @Test
    public void testLoginBothFieldsEmpty() {
        String username = "";
        String password = "";

        when(accountService.findByUsername(anyString())).thenReturn(Optional.empty());

        String result = loginController.loginProcess(model, username, password);
        assertEquals("redirect:/login/error", result);

        verify(accountService, times(1)).findByUsername(username);
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(passwordEncoder);
    }


    // Wrong password and right username
    @Test
    public void testLoginWrongPassword() {
        String username = "kiet";
        String password = "123";
        Account mockAccount = new Account(username, passwordEncoder.encode("123"));

        when(accountService.findByUsername(username)).thenReturn(Optional.of(mockAccount));
        when(passwordEncoder.matches(password, mockAccount.getPassword())).thenReturn(false);

        String result = loginController.loginProcess(model, username, password);
        assertEquals("redirect:/login/error", result);

        verify(accountService, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, mockAccount.getPassword());
    }

    // Wrong username and right password
    @Test
    public void testLoginWrongUsername() {
        String username = "kiet123";
        String password = "//Kietlai1!";

        when(accountService.findByUsername(username)).thenReturn(Optional.empty());

        String result = loginController.loginProcess(model, username, password);
        assertEquals("redirect:/login/error", result);

        verify(accountService, times(1)).findByUsername(username);
        verifyNoInteractions(passwordEncoder);
    }

}