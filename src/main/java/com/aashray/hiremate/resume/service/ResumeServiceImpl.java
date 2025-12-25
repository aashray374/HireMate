package com.aashray.hiremate.resume.service;

import com.aashray.hiremate.exception.ResumeNotFound;
import com.aashray.hiremate.exception.ResumeOfAnotherUser;
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
import java.util.Objects;
import java.util.UUID;

@Service
public class ResumeServiceImpl implements ResumeService{

    private final FileUploadUtil fileUploadUtil;
    private final ResumeRepository resumeRepository;

    public ResumeServiceImpl(FileUploadUtil fileUploadUtil, ResumeRepository resumeRepository, ResumeService resumeService) {
        this.fileUploadUtil = fileUploadUtil;
        this.resumeRepository = resumeRepository;
    }

    @Override
    public Resume uploadResume(User user, MultipartFile file, ResumeLabel label) throws IOException {
        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }
        String subDir = String.format("user-%d/%s",
                user.getId(),
                label.name().toLowerCase()
        );

        String fileName = UUID.randomUUID()+".pdf";

        fileUploadUtil.saveFile(file,subDir,fileName);

        Resume resume = Resume.builder()
                .originalFileName(file.getOriginalFilename())
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

    @Override
    public void deleteResumeWithId(User user, Long id) throws IOException {
        Resume resume = resumeRepository.findById(id).orElse(null);
        if(resume == null){
            throw new ResumeNotFound(id);
        }
        if(!Objects.equals(user.getId(), resume.getUser().getId())){
            throw new ResumeOfAnotherUser(id);
        }
        fileUploadUtil.deleteFile(resume.getStoredFileName());
        resumeRepository.deleteById(id);

    }

    @Override
    public Resume getResumeFromId(User user, Long id) {
        Resume resume = resumeRepository.findById(id).orElse(null);
        if(resume != null){
            if(resume.getUser().getId().equals(user.getId())){
                return resume;
            }
        }
        throw new ResumeNotFound(id);
    }

}
