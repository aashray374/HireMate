package com.squirtle.hiremate.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtp {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "OTP must be 6 digits")
    private String otp;
}