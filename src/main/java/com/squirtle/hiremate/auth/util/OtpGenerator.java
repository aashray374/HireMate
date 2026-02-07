package com.squirtle.hiremate.auth.util;
import java.security.SecureRandom;

public class OtpGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    public static String generateOtp() {
        int otp = SECURE_RANDOM.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
}