package com.squirtle.hiremate.common.email.service;

import com.squirtle.hiremate.common.config.DynamicEmailConfig;
import com.squirtle.hiremate.common.email.dto.ReferralEmailMessage;
import com.squirtle.hiremate.common.exception.BadRequestException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final DynamicEmailConfig dynamicEmailConfig;

    public EmailServiceImpl(JavaMailSender mailSender, DynamicEmailConfig dynamicEmailConfig) {
        this.mailSender = mailSender;
        this.dynamicEmailConfig = dynamicEmailConfig;
    }

    //    TODO: Improve the mail
    @Override
    @Async
    public void sendOtp(String to, String Otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Your OTP Code");
        msg.setText("Your OTP is: " + Otp);
        mailSender.send(msg);
    }

    @Override
    @Async
    public void sendReferralEmail(ReferralEmailMessage msg) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(msg.getTo());
            helper.setSubject(msg.getSubject());
            helper.setText(msg.getBody());

            helper.addAttachment(
                    msg.getFileName(),
                    new ByteArrayResource(msg.getFile())
            );
            JavaMailSender sender = dynamicEmailConfig.createMailSender(msg.getEmail(),msg.getPassword());
            sender.send(message);

        } catch (Exception e) {
            throw new BadRequestException("Failed to send email with attachment");
        }
    }
}
