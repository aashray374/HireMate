package com.squirtle.hiremate.auth.controller;


import com.squirtle.hiremate.auth.dto.AuthResponse;
import com.squirtle.hiremate.auth.dto.LoginRequest;
import com.squirtle.hiremate.auth.dto.SignUpRequest;
import com.squirtle.hiremate.auth.dto.VerifyOtp;
import com.squirtle.hiremate.security.jwt.JwtUtil;
import com.squirtle.hiremate.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody @Valid LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails user = userService.loadUserByUsername(request.getEmail());

        return new AuthResponse(
                jwtUtil.generateToken(
                        new User(user.getUsername(), user.getPassword(), user.getAuthorities())
                )
        );
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid SignUpRequest request){
        userService.storeInRedisandSendOtp(request);
    }

    @PostMapping("/verify-otp")
    @ResponseStatus(HttpStatus.CONTINUE)
    public ResponseEntity<String> verify(@RequestBody @Valid VerifyOtp request) {
        userService.verifyOtp(request);
        return ResponseEntity.ok("The user has been created.\n");
    }
}
