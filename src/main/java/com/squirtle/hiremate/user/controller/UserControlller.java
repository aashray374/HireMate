package com.squirtle.hiremate.user.controller;


import com.squirtle.hiremate.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserControlller {

    private final UserService userService;

    public UserControlller(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/template")
    public void addTemplate(String template, Authentication authentication){
        String email = authentication.getName();
        userService.addTemplate(template,email);
    }

    @GetMapping("/template")
    public String getTemplate(Authentication authentication){
        String email = authentication.getName();
        return userService.getTemplate(email);
    }

    @GetMapping("/generate-email")
    public String generateEmail(Authentication authentication){
        String email = authentication.getName();
        return userService.generateEmail(email);
    }
}
