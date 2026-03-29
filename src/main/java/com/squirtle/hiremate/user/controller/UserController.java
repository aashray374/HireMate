package com.squirtle.hiremate.user.controller;

import com.squirtle.hiremate.user.service.UserService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/template")
    public void addTemplate(
            @RequestBody @NotBlank String template,
            Authentication authentication
    ) {
        userService.addTemplate(template, authentication.getName());
    }

    @GetMapping("/template")
    public String getTemplate(Authentication authentication) {
        return userService.getTemplate(authentication.getName());
    }

    @GetMapping("/generate-email")
    public String generateEmail(Authentication authentication) {
        return userService.generateEmail(authentication.getName());
    }

    @PostMapping("/connect-gmail")
    public void connectEmail(
            Authentication authentication,
            @RequestBody @NotBlank String appPassword
    ) {
        userService.connectGmail(authentication.getName(), appPassword);
    }

    @DeleteMapping("/disconnect-gmail")
    public void disconnectGmail(Authentication authentication) {
        userService.disconnectGmail(authentication.getName());
    }
}