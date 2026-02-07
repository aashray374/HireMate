package com.squirtle.hiremate.resume.Controller;


import com.squirtle.hiremate.resume.dto.ResumeUploadRequest;
import com.squirtle.hiremate.resume.dto.ResumeUploadResponse;
import com.squirtle.hiremate.resume.service.ResumeService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResumeUploadResponse uploadResume(Authentication authentication,@ModelAttribute ResumeUploadRequest request){
        String email = authentication.getName();
        return resumeService.upload(email,request);
    }
}
