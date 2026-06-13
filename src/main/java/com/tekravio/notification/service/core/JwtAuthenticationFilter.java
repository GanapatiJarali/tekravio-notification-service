package com.tekravio.notification.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        String uri = request.getRequestURI();
        if (uri.startsWith("/token") || uri.startsWith("/actuator/health") || uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-resources") || uri.startsWith("/webjars")) {
            log.info("Skipping filter for public endpoint: {}", uri);
            filterChain.doFilter(request, response);
            return;
        } else {
            String authHeader = request.getHeader("Authorization");
            log.info("JWT_TOKEN :{}", authHeader);
            if (!StringUtils.hasText(authHeader)) {
                handleAuthException(response, "JWT Token is Absent");
                return;
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
                    .get("role", List.class);
            //  Store in RequestContext
            jwtRequestContext.set("email", email);
            jwtRequestContext.set("role", roles);
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

    private void handleAuthException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 401);
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", message);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}

