package com.aashray.hiremate.resume.service;

import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ResumeService {
    Resume uploadResume(User user, MultipartFile file, ResumeLabel label) throws IOException;

    Page<Resume> getAllResume(User user, Pageable page);

    Page<Resume> getAllResumeWithLabel(User user, ResumeLabel label, Pageable page);
}
