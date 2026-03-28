package com.squirtle.hiremate.user.controller;


import com.squirtle.hiremate.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/connect-gmail")
    public void connectEmail(Authentication authentication, @RequestBody String appPassword){
        String email = authentication.getName();
        userService.connectGmail(email,appPassword);
    }

    @DeleteMapping("/disconnect-gmail")
    public void disconnectGmail(Authentication authentication){
        String email = authentication.getName();
        userService.disconnectGmail(email);
    }
}
