package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.response.VerificationResponseDTO;

/**
 * Servicio de verificación de email.
 * Maneja la confirmación de tokens de verificación y activación de cuentas.
 */
public interface VerificationService {
    
    /**
     * Verifica un token de email y activa la cuenta del usuario.
     * 
     * @param token Token de verificación enviado al email
     * @return Información de la verificación exitosa
     */
    VerificationResponseDTO verifyEmail(String token);
    
    /**
     * Reenvía el token de verificación si el anterior ha expirado.
     * 
     * @param email Email del usuario
     * @return Información de la solicitud de reenvío
     */
    VerificationResponseDTO resendVerificationToken(String email);
}
