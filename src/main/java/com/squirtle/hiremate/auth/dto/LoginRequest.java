package com.squirtle.hiremate.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class LoginRequest {
    @Email
    @NonNull
    private String email;

    @NonNull
    private String password;
}
