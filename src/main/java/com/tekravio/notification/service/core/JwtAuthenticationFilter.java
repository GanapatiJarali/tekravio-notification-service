package com.tekravio.notification.service.core;

import com.tekravio.notification.service.exception.ValidationException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtRequestContext jwtRequestContext;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("login {} :", request.getRequestURL().toString());
        //login and register api not required the jwtToken
        if (request.getRequestURL().toString().contains("/login") || request.getRequestURL().toString().contains("/register")) {
            filterChain.doFilter(request, response);
            log.info("login or register{} :", request.getRequestURL().toString());
        } else {
            String authHeader = request.getHeader("Authorization");
            log.info("JWT_TOKEN :{}", authHeader);
            if (!StringUtils.hasText(authHeader)) {
                throw new ValidationException(1012, "JWT Token is Absent", "JWT Token is Absent");
            }
            String token = null;
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                token = authHeader;
            }

            log.info("token :subString : {} ", token);
            String email = jwtUtil.extractEmail(token);
            Claims claims = jwtUtil.extractAllClaims(token);
            List<String> roles = claims
                    .get("roles", List.class);
            //  Store in RequestContext
            jwtRequestContext.set("email", email);
            jwtRequestContext.set("roles", roles);
            jwtRequestContext.set("expireToken", claims.get("exp"));
            jwtRequestContext.set("JWT-Token", authHeader);
            log.info("requestContext: {}", jwtRequestContext);
            // Optional: Set Spring Security context
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }
}

