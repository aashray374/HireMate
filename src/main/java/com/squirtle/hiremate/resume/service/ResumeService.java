package com.squirtle.hiremate.resume.service;

import com.squirtle.hiremate.resume.dto.ResumeUploadRequest;
import com.squirtle.hiremate.resume.dto.ResumeUploadResponse;

public interface ResumeService {
    ResumeUploadResponse upload(String email, ResumeUploadRequest request);
}
