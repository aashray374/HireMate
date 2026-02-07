package com.squirtle.hiremate.user.service;

import com.squirtle.hiremate.auth.dto.SignUpRequest;
import com.squirtle.hiremate.auth.dto.VerifyOtp;
import com.squirtle.hiremate.auth.service.EmailService;
import com.squirtle.hiremate.auth.util.OtpGenerator;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            EmailService emailService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of()
        );
    }

    @Override
    public User getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null)    throw new RuntimeException("User Not FOund");
        return user;
    }

    @Override
    public void createNewUser(SignUpRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if(user == null){
            user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername())
                    .build();
            userRepository.save(user);
        }else{
             throw new RuntimeException("User Already Exists");
        }
    }

    @Override
    public void storeInRedisandSendOtp(SignUpRequest request) {
        try {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("User already exists");
            }
            // hash password immediately
            String passwordHash = passwordEncoder.encode(request.getPassword());
            // generate OTP
            String otp = OtpGenerator.generateOtp();
            // generate token
            String token = UUID.randomUUID().toString();
            // redis keys
            String otpKey = "otp:" + request.getEmail();
            String signupKey = "signup:" + request.getEmail();
            // OTP data (otp + username, but otp sent alone)
            Map<String, String> otpData = new HashMap<>();
            otpData.put("otp", otp);
            otpData.put("username", request.getUsername());
            // signup data
            Map<String, String> signupData = new HashMap<>();
            signupData.put("passwordHash", passwordHash);
            signupData.put("token", token);
            // store in redis with TTL 15 minutes
            redisTemplate.opsForValue().set(
                    otpKey,
                    objectMapper.writeValueAsString(otpData),
                    Duration.ofMinutes(15)
            );
            redisTemplate.opsForValue().set(
                    signupKey,
                    objectMapper.writeValueAsString(signupData),
                    Duration.ofMinutes(15)
            );
            // adding attempts functionality as well
            String attemptsKey = "otp:attempts:" + request.getEmail();
            redisTemplate.delete(attemptsKey);
            // verification link
            String link = "http://localhost:8080/auth/verify-otp?email="
                    + request.getEmail() + "&token=" + token;
            // send ONLY the OTP
            emailService.sendOtp(
                    request.getEmail(),
                    "Your OTP is: " + otp + "\n\nVerify here:\n" + link
            );
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email. Try again after some time.");
        }
    }

    @Override
    public void verifyOtp(VerifyOtp request) {
        try {
            // 1. If user already exists → error
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("User already registered");
            }

            String email = request.getEmail();
            String otpKey = "otp:" + email;
            String signupKey = "signup:" + email;
            String attemptsKey = "otp:attempts:" + email;

            // increment attempts
            Long attempts = redisTemplate.opsForValue().increment(attemptsKey);
            redisTemplate.expire(attemptsKey, Duration.ofMinutes(15));

            if (attempts != null && attempts > 5) {
                redisTemplate.delete(otpKey);
                redisTemplate.delete(signupKey);
                redisTemplate.delete(attemptsKey);
                throw new RuntimeException("Maximum OTP attempts exceeded");
            }

            // 2. Fetch OTP data
            String otpJson = redisTemplate.opsForValue().get(otpKey);
            if (otpJson == null) {
                throw new RuntimeException("OTP expired or not found");
            }

            Map<String, String> otpData =
                    objectMapper.readValue(otpJson, Map.class);

            String storedOtp = otpData.get("otp");
            String username = otpData.get("username");

            if (!storedOtp.equals(request.getOtp())) {
                throw new RuntimeException("Invalid OTP");
            }

            // 3. Fetch signup data
            String signupJson = redisTemplate.opsForValue().get(signupKey);
            if (signupJson == null) {
                throw new RuntimeException("Signup data expired");
            }

            Map<String, String> signupData =
                    objectMapper.readValue(signupJson, Map.class);

            String passwordHash = signupData.get("passwordHash");

            // create user
            User user = User.builder()
                    .email(email)
                    .password(passwordHash) // already hashed
                    .username(username)
                    .build();

            userRepository.save(user);

            // cleanup redis
            redisTemplate.delete(otpKey);
            redisTemplate.delete(signupKey);
            redisTemplate.delete(attemptsKey);
            System.out.println("The email comes out to be : " + request.getEmail());

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("OTP verification failed");
        }
    }
}
