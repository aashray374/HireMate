package com.aashray.hiremate.resume.controller;

import com.aashray.hiremate.exception.UnexpectedFileStorageError;
import com.aashray.hiremate.resume.dto.ResumeMetadata;
import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import com.aashray.hiremate.resume.mapper.ResumeMapper;
import com.aashray.hiremate.resume.service.ResumeService;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.service.UserService;
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

    @GetMapping("/")
    public Page<ResumeMetadata> getAllResume(Authentication authentication, @PageableDefault(direction = Sort.Direction.ASC,sort = "uploadedAt") Pageable page){
        User user = userService.getUserFromEmail(authentication.getName());
        Page<Resume> resumes = resumeService.getAllResume(user,page);
        return resumes.map(resumeMapper::createResponse);
    }

    @GetMapping("/label/{label}")
    public Page<ResumeMetadata> getAllResumeWithLabel(Authentication authentication, @PageableDefault(direction = Sort.Direction.ASC,sort = "uploadedAt") Pageable page, @PathVariable ResumeLabel label){
        User user = userService.getUserFromEmail(authentication.getName());
        Page<Resume> resumes = resumeService.getAllResumeWithLabel(user,label,page);
        return resumes.map(resumeMapper::createResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResume(Authentication authentication,@PathVariable Long id){
        try{
            User user = userService.getUserFromEmail(authentication.getName());
            resumeService.deleteResumeWithId(user,id);
        }catch (IOException e){
            throw new UnexpectedFileStorageError(e.getMessage());
        }
    }
}
