package com.squirtle.hiremate.auth.service;

public interface EmailService {
    void sendOtp(String to, String Otp);
}
