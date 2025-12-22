package com.aashray.hiremate.resume.controller;

import com.aashray.hiremate.exception.UnexpectedFileStorageError;
import com.aashray.hiremate.resume.dto.ResumeMetadata;
import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import com.aashray.hiremate.resume.mapper.ResumeMapper;
import com.aashray.hiremate.resume.service.ResumeService;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

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
    public ResumeMetadata uploadResume(@RequestParam("file")MultipartFile file, @RequestParam("label")ResumeLabel label, Authentication authentication){
        try{
            String email = authentication.getName();
            User user = userService.getUserFromEmail(email);
            Resume resume = resumeService.uploadResume(user,file,label);
            return resumeMapper.createResponse(resume);
        }catch (IOException e){
            throw new UnexpectedFileStorageError(e.getMessage());
        }
    }
}
