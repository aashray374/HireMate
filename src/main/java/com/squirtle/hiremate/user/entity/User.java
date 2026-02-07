package com.squirtle.hiremate.user.entity;


import com.squirtle.hiremate.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "user_table"
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "resume_id")
    private Resume resume = null;

    @Column(updatable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private String template;

    @PrePersist
    public void init(){
        OffsetDateTime time = OffsetDateTime.now();
        this.createdAt = time;
        this.updatedAt = time;
    }

    @PreUpdate
    public void update(){
        this.updatedAt = OffsetDateTime.now();
    }

}
