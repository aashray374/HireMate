package com.aashray.hiremate.resume.dto;

import com.aashray.hiremate.resume.entity.ResumeLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeMetadata {
    private Long id;
    private String originalFileName;
    private String storedFileName;
    private OffsetDateTime uploadedAt;
    private ResumeLabel label;
}
