package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.response.UserResponseDTO;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.mapper.UserMapper;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import com.codefactory.reservasmsauthservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
