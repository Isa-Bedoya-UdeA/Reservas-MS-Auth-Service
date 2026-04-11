package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO findByEmail(String email);
    boolean existsByEmail(String email);
}
