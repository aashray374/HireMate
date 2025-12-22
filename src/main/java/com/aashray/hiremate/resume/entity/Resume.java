package com.aashray.hiremate.resume.entity;


import com.aashray.hiremate.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @NotBlank
    @Column(nullable = false)
    private String originalFileName;

    @NotBlank
    @Column(nullable = false)
    private String storedFileName;

    @Enumerated(EnumType.STRING)
    private ResumeLabel label;

    private OffsetDateTime uploadedAt;


    @PrePersist
    public void init(){
        this.uploadedAt = OffsetDateTime.now();
    }
}
