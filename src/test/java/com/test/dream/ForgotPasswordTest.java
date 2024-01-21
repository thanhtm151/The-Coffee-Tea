package com.test.dream;

import com.backend.dream.config.EmailService;
import com.backend.dream.controller.ForgotPasswordController;
import com.backend.dream.dto.AccountDTO;
import com.backend.dream.dto.TokenDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.Token;
import com.backend.dream.mapper.AccountMapper;
import com.backend.dream.repository.TokenRepository;
import com.backend.dream.service.AccountService;
import com.backend.dream.service.TokenService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForgotPasswordTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmailService emailService;


    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private ForgotPasswordController forgotPasswordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEmptyEmailAndPassword() throws Exception {
        Model model = Mockito.mock(Model.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        String result = forgotPasswordController.processForgotPasswordForm("", "", model, session);
        assertEquals("/user/security/forgotPass", result);
        verify(model).addAttribute(eq("message"), anyString());
        verifyNoInteractions(tokenService, emailService);
    }

    @Test
    public void testProcessForgotPassword_CorrectUsernameAndEmail() throws Exception {
        String username = "kiet";
        String email = "kietlai667@gmail.com";
        Account account = new Account();
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken("123456");
        when(accountService.findByUsernameAndEmail(username, email)).thenReturn(account);
        when(tokenService.createTokenForUser(account)).thenReturn(tokenDTO);
        Model model = Mockito.mock(Model.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        String result = forgotPasswordController.processForgotPasswordForm(username, email, model, session);
        assertEquals("/user/security/verifi", result);
        verify(model).addAttribute(eq("message"), anyString());
        verify(session).setAttribute(eq("tokenValue"), anyString());
        verify(emailService).sendEmailTokenPass(eq(email), anyString(), isNull());
        verifyNoMoreInteractions(model, session, emailService);
    }

    @Test
    public void testProcessForgotPassword_IncorrectEmail() throws Exception {
        String username = "kiet";
        String email = "kietlai@gmail.com.com";
        when(accountService.findByUsernameAndEmail(username, email)).thenReturn(null);
        Model model = Mockito.mock(Model.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        String result = forgotPasswordController.processForgotPasswordForm(username, email, model, session);
        assertEquals("/user/security/forgotPass", result);
        verify(model).addAttribute(eq("message"), anyString());
        verifyNoInteractions(session, emailService);
        verifyNoMoreInteractions(model, session, emailService, accountService);
    }

    @Test
    public void testProcessVerify_InvalidToken() {
        String tokenSentInEmail = "123456";
        Token validToken = new Token();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
        validToken.setExpiredDate(expiration);
        String inputToken = "654321";
        HttpSession session = Mockito.mock(HttpSession.class);
        Model model = Mockito.mock(Model.class);
        when(session.getAttribute("tokenValue")).thenReturn(tokenSentInEmail);
        String result = forgotPasswordController.processVerifiForm(inputToken, model, session);
        assertEquals("/user/security/verifi", result);
        verify(model).addAttribute(eq("message"), anyString());
        verifyNoMoreInteractions(model, session);
    }


    @Test
    public void testProcessVerify_ValidOTP() {
        String correctToken = "123456";
        HttpSession session = Mockito.mock(HttpSession.class);
        Model model = Mockito.mock(Model.class);
        Token mockedToken = Mockito.mock(Token.class);
        when(session.getAttribute("tokenValue")).thenReturn(correctToken);
        when(tokenService.findByToken(correctToken)).thenReturn(mockedToken);
        when(mockedToken.getExpiredDate()).thenReturn(LocalDateTime.now().plusMinutes(10));
        String result = forgotPasswordController.processVerifiForm(correctToken, model, session);
        assertEquals("redirect:/confirmPass", result);
        verifyNoMoreInteractions(model, session);
    }

    @Test
    public void testProcessResetPassword_ConfirmPasswordMatch() {
        String password = "Kietlai1!";
        String confirmPassword = "Kietlai1!";
        HttpSession session = Mockito.mock(HttpSession.class);
        Model model = Mockito.mock(Model.class);
        Token mockedToken = Mockito.mock(Token.class);
        Account mockedAccount = Mockito.mock(Account.class);
        when(session.getAttribute(eq("tokenValue"))).thenReturn("123456");
        when(tokenService.findByToken("123456")).thenReturn(mockedToken);
        when(mockedToken.getAccount()).thenReturn(mockedAccount);
        String result = forgotPasswordController.processResetPassword(password, confirmPassword, model, session);
        assertEquals("/user/security/login", result);
        verify(model).addAttribute(eq("message"), anyString());
        verifyNoMoreInteractions(model, session);
    }



    @Test
    public void testProcessResetPassword_PasswordsDoNotMatch() {
        String password = "Kietlt1!";
        String confirmPassword = "Kietlt123!";
        HttpSession session = Mockito.mock(HttpSession.class);
        Model model = Mockito.mock(Model.class);
        Token mockedToken = Mockito.mock(Token.class);
        Account mockedAccount = Mockito.mock(Account.class);
        when(session.getAttribute("tokenValue")).thenReturn("validToken");
        when(tokenService.findByToken("validToken")).thenReturn(mockedToken);
        when(mockedToken.getAccount()).thenReturn(mockedAccount);
        String result = forgotPasswordController.processResetPassword(password, confirmPassword, model, session);
        assertEquals("/user/security/confirmPass", result);
        verify(model).addAttribute(eq("message"), anyString());
        verifyNoMoreInteractions(model, session, accountService);
    }



}
