package com.squirtle.hiremate.contacts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Contact {

    @Id
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String company;
}
