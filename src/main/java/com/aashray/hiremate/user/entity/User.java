package com.aashray.hiremate.user.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NonNull
    @Column(unique = true)
    private String email;

    private String password;

    @NonNull
    @Size(min = 3,max = 50)
    private String firstName;

    @NonNull
    @Size(min = 3,max = 50)
    private String lastName;

    private OffsetDateTime createdAt;

    @PrePersist
    public void init(){
        createdAt = OffsetDateTime.now();
    }

}
