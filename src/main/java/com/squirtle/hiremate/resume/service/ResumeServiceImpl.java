package com.squirtle.hiremate.resume.service;

import com.squirtle.hiremate.resume.dto.ResumeUploadRequest;
import com.squirtle.hiremate.resume.dto.ResumeUploadResponse;
import com.squirtle.hiremate.resume.entity.Resume;
import com.squirtle.hiremate.resume.repository.ResumeRepository;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.service.UserService;
import com.squirtle.hiremate.utils.CloudinaryUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ResumeServiceImpl implements ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeServiceImpl.class);

    private final ResumeRepository repository;
    private final CloudinaryUtil cloudinaryUtil;
    private final UserService userService;

    @Value("${cloudinary.path.resume}")
    private String resumeFolderName;

    public ResumeServiceImpl(
            ResumeRepository repository,
            CloudinaryUtil cloudinaryUtil,
            UserService userService
    ) {
        this.repository = repository;
        this.cloudinaryUtil = cloudinaryUtil;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ResumeUploadResponse upload(String email, ResumeUploadRequest request) {

        log.info("Starting resume upload for user: {}", email);

        User user = userService.getUserByEmail(email);

        if (user.getResume() != null) {
            log.info(
                    "Existing resume found for user: {}. Deleting publicId={}",
                    email,
                    user.getResume().getPublicId()
            );

            cloudinaryUtil.deleteFile(user.getResume().getPublicId());
            user.setResume(null);
        }

        log.debug("Uploading resume to Cloudinary folder: {}", resumeFolderName);

        Map<String, String> uploadResult =
                cloudinaryUtil.uploadFile(request.getFile(), resumeFolderName);

        log.info(
                "Resume uploaded successfully for user: {}. publicId={}",
                email,
                uploadResult.get("publicId")
        );

        Resume resume = Resume.builder()
                .url(uploadResult.get("url"))
                .publicId(uploadResult.get("publicId"))
                .user(user)
                .updatedAt(java.time.OffsetDateTime.now())
                .build();

        repository.save(resume);
        user.setResume(resume);

        log.info("Resume entity persisted for user: {}", email);

        return new ResumeUploadResponse(resume.getUrl());
    }
}
