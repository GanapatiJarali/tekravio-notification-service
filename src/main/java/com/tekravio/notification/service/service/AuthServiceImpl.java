package com.tekravio.notification.service.service;


import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.common.Result;
import com.tekravio.notification.service.core.JwtUtil;
import com.tekravio.notification.service.dto.LoginRequest;
import com.tekravio.notification.service.dto.LoginResponse;
import com.tekravio.notification.service.exception.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {


    private final PasswordEncoder passwordEncoder;
    @Value("${notification.auth.client-id}")
    private String clientId;
    @Value("${notification.auth.client-secret}")
    private String clientSceret;

    @Value("${notification.system.roles}")
    private List<String> roleList;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public BaseResponse<LoginResponse> login(LoginRequest request) {
        if (!request.getClientId().equals(clientId) || !request.getClientSecret().equals(clientSceret)) {
            throw new ValidationException(1007, "Invalid credentials....!", "Invalid credentials....!");
        }
        if (roleList.contains(request.getRole())) {
            throw new ValidationException(1010, "roles not found..!", "roles not found..!");
        }
        String token = jwtUtil.generateToken(clientId, clientSceret, request.getRole());
        Result response = new Result();
        response.setSuccessCode(0);
        response.setSuccessDescription("Success");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(new LoginResponse(token, clientId));
        baseResponse.setResult(response);
        return baseResponse;
    }
}
