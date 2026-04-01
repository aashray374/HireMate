package com.squirtle.hiremate.auth.service;

import com.squirtle.hiremate.common.email.service.EmailService;
import com.squirtle.hiremate.common.utils.OtpGenerator;
import com.squirtle.hiremate.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final StringRedisTemplate redis;
    private final EmailService emailService;

    private static final String OTP_PREFIX = "OTP:";

    @Override
    public void generateAndSend(String email) {
        String otp = OtpGenerator.generateOtp();

        redis.opsForValue().set(
                OTP_PREFIX + email,
                otp,
                Duration.ofMinutes(5)
        );

        emailService.sendOtp(email, otp);
    }

    @Override
    public boolean verify(String email, String otp) {
        String key = OTP_PREFIX + email;
        String stored = redis.opsForValue().get(key);

        if (stored == null) {
            throw new BadRequestException("OTP expired or not found");
        }

        if (!stored.equals(otp)) {
            throw new BadRequestException("Invalid OTP");
        }

        redis.delete(key); // prevent reuse
        return true;
    }
}