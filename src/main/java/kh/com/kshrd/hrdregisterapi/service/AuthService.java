package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.LoginRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.request.RegisterRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.TokenResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UserResponse;

import java.util.UUID;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    TokenResponse login(LoginRequest request) throws Exception;

}
