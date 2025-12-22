package com.aashray.hiremate.resume.service;

import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import com.aashray.hiremate.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ResumeService {
    Resume uploadResume(User user, MultipartFile file, ResumeLabel label) throws IOException;
}
