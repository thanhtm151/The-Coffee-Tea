package com.backend.dream.config;

import com.backend.dream.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
        @Autowired
        private JavaMailSender mailSender;
        @Autowired
        private TokenService tokenService;
        @Autowired
        private TemplateEngine templateEngine;

        public void sendEmailTokenPass(String to, String token, String fullname) throws MessagingException {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                Context context = new Context();
                context.setVariable("token", token);
                context.setVariable("fullname", fullname);

                String htmlContent = templateEngine.process("/user/security/resetPasswordEmailTemplate", context);

                helper.setTo(to);
                helper.setSubject("Reset Password");
                helper.setText(htmlContent, true);

                mailSender.send(message);
        }
}
