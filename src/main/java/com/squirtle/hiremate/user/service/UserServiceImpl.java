package com.squirtle.hiremate.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squirtle.hiremate.auth.dto.SignUpRequest;
import com.squirtle.hiremate.auth.dto.VerifyOtp;
import com.squirtle.hiremate.auth.service.EmailService;
import com.squirtle.hiremate.auth.util.OtpGenerator;
import com.squirtle.hiremate.exception.BadRequestException;
import com.squirtle.hiremate.exception.ResourceNotFoundException;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.repository.UserRepository;
import com.squirtle.hiremate.utils.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeminiService geminiService;

    private static final String OTP_PREFIX = "otp:";
    private static final String SIGNUP_PREFIX = "signup:";
    private static final String ATTEMPT_PREFIX = "otp:attempts:";

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of()
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void addTemplate(String template, String email) {
        User user = getUserByEmail(email);
        user.setTemplate(template);
        userRepository.save(user);
    }

    @Override
    public String getTemplate(String email) {
        return getUserByEmail(email).getTemplate();
    }

    @Override
    public String generateEmail(String email) {
        User user = getUserByEmail(email);

        if (user.getResume() == null || user.getResume().getUrl() == null) {
            throw new BadRequestException("Resume not uploaded");
        }

        String prompt = "Write a referral email for jobId:12345 company:Google role:SDE Intern. Resume: "
                + user.getResume().getUrl();

        return geminiService.getAnswer(prompt);
    }

    @Override
    public void connectGmail(String email, String appPassword) {
        User user = getUserByEmail(email);

        if (appPassword.isBlank()) {
            throw new BadRequestException("App password cannot be empty");
        }

        user.setAppPassword(appPassword);
        userRepository.save(user);
    }

    @Override
    public void disconnectGmail(String email) {
        User user = getUserByEmail(email);

        if (user.getAppPassword() == null || user.getAppPassword().isBlank()) {
            throw new BadRequestException("Gmail not connected");
        }

        user.setAppPassword(null);
        userRepository.save(user);
    }

    @Override
    public void storeInRedisandSendOtp(SignUpRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("User already exists");
        }

        try {
            String passwordHash = passwordEncoder.encode(request.getPassword());
            String otp = OtpGenerator.generateOtp();
            String token = UUID.randomUUID().toString();

            String otpKey = OTP_PREFIX + request.getEmail();
            String signupKey = SIGNUP_PREFIX + request.getEmail();

            Map<String, String> otpData = Map.of(
                    "otp", otp,
                    "username", request.getUsername()
            );

            Map<String, String> signupData = Map.of(
                    "passwordHash", passwordHash,
                    "token", token
            );

            redisTemplate.opsForValue().set(otpKey, objectMapper.writeValueAsString(otpData), Duration.ofMinutes(15));
            redisTemplate.opsForValue().set(signupKey, objectMapper.writeValueAsString(signupData), Duration.ofMinutes(15));

            emailService.sendOtp(request.getEmail(), otp);

        } catch (Exception e) {
            log.error("OTP sending failed", e);
            throw new BadRequestException("Failed to send OTP");
        }
    }

    @Override
    public boolean verifyOtp(VerifyOtp request) {

        String email = request.getEmail();
        String otpKey = OTP_PREFIX + email;
        String signupKey = SIGNUP_PREFIX + email;
        String attemptsKey = ATTEMPT_PREFIX + email;

        Long attempts = redisTemplate.opsForValue().increment(attemptsKey);
        redisTemplate.expire(attemptsKey, Duration.ofMinutes(15));

        if (attempts != null && attempts > 5) {
            throw new BadRequestException("Too many attempts");
        }

        try {
            String otpJson = redisTemplate.opsForValue().get(otpKey);
            if (otpJson == null) {
                throw new BadRequestException("OTP expired");
            }

            Map<String, String> otpData = objectMapper.readValue(otpJson, Map.class);

            if (!otpData.get("otp").equals(request.getOtp())) {
                throw new BadRequestException("Invalid OTP");
            }

            String signupJson = redisTemplate.opsForValue().get(signupKey);
            if (signupJson == null) {
                throw new BadRequestException("Signup expired");
            }

            Map<String, String> signupData = objectMapper.readValue(signupJson, Map.class);

            User user = User.builder()
                    .email(email)
                    .username(otpData.get("username"))
                    .password(signupData.get("passwordHash"))
                    .build();

            userRepository.save(user);

            redisTemplate.delete(List.of(otpKey, signupKey, attemptsKey));

            return true;

        } catch (Exception e) {
            log.error("OTP verification failed", e);
            throw new BadRequestException("OTP verification failed");
        }
    }
}