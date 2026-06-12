package com.tekravio.notification.service.service;


import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.dto.LoginRequest;
import com.tekravio.notification.service.dto.LoginResponse;

public interface AuthService {
     BaseResponse<LoginResponse> login(LoginRequest request);
}
