package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.exception.BusinessException;
import com.codefactory.reservasmsauthservice.exception.EmailAlreadyExistsException;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import com.codefactory.reservasmsauthservice.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementación de UserAuthService.
 * Centraliza lógica compartida para validación de email y contraseña.
 */
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void validateEmailAndPassword(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está en uso");
        }
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public void validateEmailVerification(User user) {
        if (Boolean.FALSE.equals(user.getEmailVerificado())) {
            throw new BusinessException("Tu correo electrónico no ha sido verificado. Por favor, verifica tu email antes de iniciar sesión.");
        }
    }
}
