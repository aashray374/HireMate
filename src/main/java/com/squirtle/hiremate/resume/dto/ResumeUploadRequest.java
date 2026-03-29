package com.squirtle.hiremate.resume.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResumeUploadRequest {

    @NotNull(message = "File is required")
    private MultipartFile file;
}
