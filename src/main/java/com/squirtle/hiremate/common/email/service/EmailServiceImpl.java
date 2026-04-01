package com.squirtle.hiremate.common.email.service;

import com.squirtle.hiremate.common.email.dto.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired private JavaMailSender mailSender;

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
    public void sendReferralEmail(EmailMessage message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        JavaMailSender mailSender = message.getMailSender();
        msg.setText(message.getBody());
        msg.setTo(message.getTo());
        msg.setSubject(message.getSubject());
        mailSender.send(msg);
    }
}
