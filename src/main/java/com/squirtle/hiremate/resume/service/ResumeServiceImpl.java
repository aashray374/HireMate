package com.squirtle.hiremate.resume.service;

import com.squirtle.hiremate.common.exception.BadRequestException;
import com.squirtle.hiremate.resume.dto.ResumeUploadRequest;
import com.squirtle.hiremate.resume.dto.ResumeUploadResponse;
import com.squirtle.hiremate.resume.entity.Resume;
import com.squirtle.hiremate.resume.repository.ResumeRepository;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.service.UserService;
import com.squirtle.hiremate.common.utils.CloudinaryUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository repository;
    private final CloudinaryUtil cloudinaryUtil;
    private final UserService userService;

    @Value("${cloudinary.path.resume}")
    private String resumeFolderName;

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    @Override
    @Transactional
    public ResumeUploadResponse upload(String email, ResumeUploadRequest request) {

        MultipartFile file = request.getFile();

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds 2MB");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("application/pdf") &&
                        !contentType.equals("application/msword") &&
                        !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new BadRequestException("Only PDF/DOC/DOCX files allowed");
        }

        User user = userService.getUserByEmail(email);

        try {
            // delete old resume
            if (user.getResume() != null) {
                cloudinaryUtil.deleteFile(user.getResume().getPublicId());
                user.setResume(null);
            }

            // upload new file
            Map<String, String> uploadResult =
                    cloudinaryUtil.uploadFile(file, resumeFolderName);

            Resume resume = Resume.builder()
                    .url(uploadResult.get("url"))
                    .publicId(uploadResult.get("publicId"))
                    .user(user)
                    .build();

            repository.save(resume);
            user.setResume(resume);

            return new ResumeUploadResponse(resume.getUrl());

        } catch (Exception e) {
            log.error("Resume upload failed for user: {}", email, e);
            throw new BadRequestException("Resume upload failed");
        }
    }
}