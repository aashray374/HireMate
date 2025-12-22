package com.aashray.hiremate.auth.dto;


import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NonNull
    @Email
    private String email;

    @NonNull
    private String password;
}
