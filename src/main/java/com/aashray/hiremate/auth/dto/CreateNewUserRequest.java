package com.aashray.hiremate.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewUserRequest {

    @Email
    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    @Size(min = 3,max = 50)
    private String firstName;

    @NonNull
    @Size(min = 3,max = 50)
    private String lastName;

}
