package com.squirtle.hiremate.auth.service;

import com.squirtle.hiremate.auth.util.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class OtpServiceImpl implements OtpService {
    private final StringRedisTemplate redis;
    private final EmailService emailService;

    @Autowired
    public OtpServiceImpl(StringRedisTemplate redis, EmailService emailService) {
        this.redis = redis;
        this.emailService = emailService;
    }

    @Override
    public void generateAndSend(String email) {
        String otp = OtpGenerator.generateOtp();
        redis.opsForValue().set(email, otp, Duration.ofMinutes(5)); // expire 5m
        emailService.sendOtp(email, otp);
    }

    @Override
    public boolean verify(String email, String otp) {
        String stored = redis.opsForValue().get(email);
        return stored != null && stored.equals(otp);
    }
}
