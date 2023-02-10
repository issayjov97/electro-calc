package com.example.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;

@Service
public class EmailNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    EmailNotificationService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public boolean sendEmail(String content, String to) {
        try {
            var mimeMessage = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, true);
            var context = new Context();
            context.setVariable("name", to);
            context.setVariable("content", content);
            helper.setSubject("OTP pro obnovení hesla");
            helper.setTo(to);
            helper.setText(templateEngine.process("notification", context), true);
            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            logger.error("Failed to send email to {}", to, e);
            return false;
        }
        return true;
    }
}
