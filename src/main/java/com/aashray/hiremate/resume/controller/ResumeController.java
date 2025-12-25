package com.aashray.hiremate.resume.controller;

import com.aashray.hiremate.exception.UnexpectedFileStorageError;
import com.aashray.hiremate.resume.dto.ResumeMetadata;
import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import com.aashray.hiremate.resume.mapper.ResumeMapper;
import com.aashray.hiremate.resume.service.ResumeService;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final UserService userService;
    private final ResumeService resumeService;
    private final ResumeMapper resumeMapper;

    public ResumeController(UserService userService, ResumeService resumeService, ResumeMapper resumeMapper) {
        this.userService = userService;
        this.resumeService = resumeService;
        this.resumeMapper = resumeMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResumeMetadata uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("label") ResumeLabel label,
            Authentication authentication
    ) {
        String email = authentication.getName();
        log.info("Request received to upload resume for user: [{}] with label: [{}]", email, label);

        try {
            User user = userService.getUserFromEmail(email);
            Resume resume = resumeService.uploadResume(user, file, label);

            log.info("Resume uploaded successfully. Resume ID: [{}], Filename: [{}]", resume.getId(), file.getOriginalFilename());

            return resumeMapper.createResponse(resume);
        } catch (IOException e) {
            log.error("Failed to store resume file for user: [{}]", email, e);
            throw new UnexpectedFileStorageError(e.getMessage());
        }
    }

    @GetMapping
    public Page<ResumeMetadata> getAllResume(
            Authentication authentication,
            @PageableDefault(direction = Sort.Direction.ASC, sort = "uploadedAt") Pageable page
    ) {
        String email = authentication.getName();
        log.info("Fetching all resumes for user: [{}], Page: [{}]", email, page.getPageNumber());

        User user = userService.getUserFromEmail(email);
        Page<Resume> resumes = resumeService.getAllResume(user, page);

        log.debug("Found {} resumes for user: [{}]", resumes.getTotalElements(), email);

        return resumes.map(resumeMapper::createResponse);
    }

    @GetMapping("/{id}")
    public ResumeMetadata getResumeFromId(@PathVariable Long id,Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        return resumeMapper.createResponse(resumeService.getResumeFromId(user,id));
    }

    @GetMapping("/label/{label}")
    public Page<ResumeMetadata> getAllResumeWithLabel(
            Authentication authentication,
            @PageableDefault(direction = Sort.Direction.ASC, sort = "uploadedAt") Pageable page,
            @PathVariable ResumeLabel label
    ) {
        String email = authentication.getName();
        log.info("Fetching resumes with label: [{}] for user: [{}], Page: [{}]", label, email, page.getPageNumber());

        User user = userService.getUserFromEmail(email);
        Page<Resume> resumes = resumeService.getAllResumeWithLabel(user, label, page);

        return resumes.map(resumeMapper::createResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResume(Authentication authentication, @PathVariable Long id) {
        String email = authentication.getName();
        log.info("Request received to delete resume ID: [{}] for user: [{}]", id, email);

        try {
            User user = userService.getUserFromEmail(email);
            resumeService.deleteResumeWithId(user, id);
            log.info("Resume ID: [{}] deleted successfully for user: [{}]", id, email);
        } catch (IOException e) {
            log.error("Failed to delete resume file for ID: [{}]", id, e);
            throw new UnexpectedFileStorageError(e.getMessage());
        }
    }
}