package com.vedha.service;

import com.vedha.dto.Login;
import com.vedha.dto.LoginResponse;
import com.vedha.dto.LogoutResponse;
import com.vedha.dto.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    Users register(Users user);

    LoginResponse login(Login login);

    LogoutResponse logout(HttpServletRequest request, HttpServletResponse response);

    LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
