package com.aashray.hiremate.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    @NonNull
    private Long id;

    @Email
    @NonNull
    private String email;

    @NonNull
    @Size(min = 3,max = 50)
    private String firstName;

    @NonNull
    @Size(min = 3,max = 50)
    private String lastName;

    @NonNull
    private OffsetDateTime cratedAt;
}
