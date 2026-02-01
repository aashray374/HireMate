package com.squirtle.hiremate.security.jwt;

import com.squirtle.hiremate.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        try{
            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {

                String token = header.substring(7);
                String username = jwtUtil.getEmail(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails user = userService.loadUserByUsername(username);
                    if (jwtUtil.verifyToken(token, user.getUsername())) {

                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                        user, null, user.getAuthorities());
                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(auth);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
