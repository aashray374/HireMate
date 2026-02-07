package com.squirtle.hiremate.resume.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResumeUploadRequest {
    private MultipartFile file;
}
