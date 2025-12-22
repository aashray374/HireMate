package com.aashray.hiremate.resume.service;

import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import com.aashray.hiremate.resume.repository.ResumeRepository;
import com.aashray.hiremate.resume.util.FileUploadUtil;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeServiceImpl implements ResumeService{

    private final FileUploadUtil fileUploadUtil;
    private final ResumeRepository resumeRepository;
    private final UserService userService;

    public ResumeServiceImpl(FileUploadUtil fileUploadUtil, ResumeRepository resumeRepository, UserService userService) {
        this.fileUploadUtil = fileUploadUtil;
        this.resumeRepository = resumeRepository;
        this.userService = userService;
    }

    public Resume uploadResume(User user, MultipartFile file, ResumeLabel label) throws IOException {
        String subDir = String.format("user-%d/%s",
                user.getId(),
                label.name().toLowerCase()
        );

        String fileName = String.format("%s_resume_v%d",
                label.name().toLowerCase(),
                resumeRepository.findAllByUserAndLabel(user,label).size()+1
        );

        String fullPath = fileUploadUtil.saveFile(file,subDir,fileName);

        Resume resume = Resume.builder()
                .originalFileName(file.getName())
                .storedFileName(fullPath)
                .label(label)
                .user(user)
                .build();

        return resumeRepository.save(resume);
    }

}
