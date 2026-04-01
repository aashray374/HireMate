package com.squirtle.hiremate.common.email.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReferralEmailMessage {
    private String to;
    private String subject;
    private String body;
    private String email;
    private String password;
    private byte[] file;
    private String fileName;
}
