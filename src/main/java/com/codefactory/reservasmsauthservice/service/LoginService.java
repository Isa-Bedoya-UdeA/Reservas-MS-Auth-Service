package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.request.LoginRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.LogoutRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.RefreshTokenRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.LoginResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    LoginResponseDTO login(LoginRequestDTO request, HttpServletRequest httpRequest);
    LoginResponseDTO refreshToken(RefreshTokenRequestDTO request);
    void logout(LogoutRequestDTO request);
}
