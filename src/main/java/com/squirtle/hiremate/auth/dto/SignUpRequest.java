package com.squirtle.hiremate.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;


@Data
@AllArgsConstructor
public class SignUpRequest {
    @Email
    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    @Size(min = 3,max = 50)
    private String username;

}
