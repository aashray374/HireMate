package com.aashray.hiremate.resume.service;

import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import com.aashray.hiremate.resume.repository.ResumeRepository;
import com.aashray.hiremate.resume.util.FileUploadUtil;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ResumeServiceImpl implements ResumeService{

    private final FileUploadUtil fileUploadUtil;
    private final ResumeRepository resumeRepository;

    public ResumeServiceImpl(FileUploadUtil fileUploadUtil, ResumeRepository resumeRepository) {
        this.fileUploadUtil = fileUploadUtil;
        this.resumeRepository = resumeRepository;
    }

    public Resume uploadResume(User user, MultipartFile file, ResumeLabel label) throws IOException {
        String subDir = String.format("user-%d/%s",
                user.getId(),
                label.name().toLowerCase()
        );

        String fileName = UUID.randomUUID()+".pdf";

        fileUploadUtil.saveFile(file,subDir,fileName);

        Resume resume = Resume.builder()
                .originalFileName(file.getName())
                .storedFileName(subDir+"/"+fileName)
                .label(label)
                .user(user)
                .build();

        return resumeRepository.save(resume);
    }

    @Override
    public Page<Resume> getAllResume(User user, Pageable page) {
        return resumeRepository.findAllByUser(user,page);
    }

    @Override
    public Page<Resume> getAllResumeWithLabel(User user, ResumeLabel label, Pageable page) {
        return resumeRepository.findAllByUserAndLabel(user,label,page);
    }

}
