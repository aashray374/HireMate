package com.squirtle.hiremate.resume.entity;


import com.squirtle.hiremate.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String url;

    @Column(nullable = false)
    private String publicId;

    @OneToOne(fetch = FetchType.EAGER,mappedBy = "resume")
    private User user;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void init(){
        this.updatedAt = OffsetDateTime.now();
    }

}
