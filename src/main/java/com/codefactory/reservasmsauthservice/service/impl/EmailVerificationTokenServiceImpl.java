package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.entity.EmailVerificationToken;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.InvalidVerificationTokenException;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.repository.EmailVerificationTokenRepository;
import com.codefactory.reservasmsauthservice.service.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementación de EmailVerificationTokenService.
 * Gestiona la creación, validación y revocación de tokens de verificación de email.
 */
@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    private static final int TOKEN_EXPIRY_HOURS = 24;
    
    private final EmailVerificationTokenRepository tokenRepository;

    @Override
    @Transactional
    public String generateToken(User user) {
        // Eliminar token anterior no utilizado si existe
        tokenRepository.deleteByUser_IdUsuario(user.getIdUsuario());
        
        // Generar nuevo token UUID único
        String tokenValue = UUID.randomUUID().toString();
        
        // Crear nueva entidad
        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setToken(tokenValue);
        token.setFechaExpiracion(LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS));
        token.setUsado(false);
        
        tokenRepository.save(token);
        return tokenValue;
    }

    @Override
    @Transactional(readOnly = true)
    public EmailVerificationToken validateToken(String token) {
        return tokenRepository.findValidByToken(token)
                .orElseThrow(() -> new InvalidVerificationTokenException("Token de verificación inválido, expirado o ya utilizado", true));
    }

    @Override
    @Transactional
    public boolean confirmToken(String token) {
        EmailVerificationToken verificationToken = validateToken(token);

        if (verificationToken.getUsado()) {
            throw new InvalidVerificationTokenException("El token de verificación ya ha sido utilizado", true);
        }

        verificationToken.setUsado(true);
        tokenRepository.save(verificationToken);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public EmailVerificationToken getActiveTokenByUserId(UUID userId) {
        return tokenRepository.findByUser_IdUsuarioAndUsadoFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un token de verificación activo para el usuario"));
    }

    @Override
    @Transactional
    public void deleteTokensByUserId(UUID userId) {
        tokenRepository.deleteByUser_IdUsuario(userId);
    }
}
