package com.squirtle.hiremate.common.security.jwt;

import com.squirtle.hiremate.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                String email = jwtUtil.getEmail(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails user = userService.loadUserByUsername(email);

                    if (jwtUtil.verifyToken(token, email)) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        user, null, user.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }

        } catch (Exception e) {
            log.error("JWT authentication failed", e);
        }

        filterChain.doFilter(request, response);
    }
}