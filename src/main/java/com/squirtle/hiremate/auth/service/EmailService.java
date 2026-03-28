package com.squirtle.hiremate.auth.service;

import org.springframework.mail.javamail.JavaMailSender;

public interface EmailService {
    void sendOtp(String to, String Otp);
    void SendReferralEmail(String to, String subject, String body, JavaMailSender mailSender);
}
