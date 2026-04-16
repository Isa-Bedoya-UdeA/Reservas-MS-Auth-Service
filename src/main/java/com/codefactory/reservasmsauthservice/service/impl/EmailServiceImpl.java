package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Implementation of EmailService.
 * Sends verification emails using JavaMailSender and Thymeleaf templates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${email.username}")
    private String emailUsername;

    @Override
    public void sendVerificationEmail(String to, String name, String token) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Verifica tu correo electrónico - " + appName);
            helper.setFrom(emailUsername);

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("verificationUrl", frontendUrl + "/verify?token=" + token);
            context.setVariable("appName", appName);

            // Process HTML template
            String htmlContent = templateEngine.process("email-verification", context);
            helper.setText(htmlContent, true);

            // Send email
            javaMailSender.send(message);
            log.info("Verification email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", to, e);
            throw new RuntimeException("Failed to send verification email: " + e.getMessage(), e);
        }
    }
}
