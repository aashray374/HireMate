package com.squirtle.hiremate.common.email.dto;

import lombok.*;
import org.springframework.mail.javamail.JavaMailSender;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailMessage {
    private String to;
    private String subject;
    private String body;
    private JavaMailSender mailSender;
}
