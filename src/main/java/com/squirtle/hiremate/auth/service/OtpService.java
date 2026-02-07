package com.squirtle.hiremate.auth.service;

public interface OtpService {
    void generateAndSend(String email);
    boolean verify(String email, String otp);
}
