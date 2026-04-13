package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.entity.EmailVerificationToken;
import com.codefactory.reservasmsauthservice.entity.User;
import java.util.UUID;

/**
 * Servicio para gestionar tokens de verificación de email.
 * Responsable de crear, validar, regenerar y eliminar tokens.
 */
public interface EmailVerificationTokenService {
    
    /**
     * Crea un nuevo token de verificación para un usuario.
     * Si ya existe un token no utilizado, lo revoca.
     * @param user Usuario para el cual crear el token
     * @return Token de verificación generado
     */
    String generateToken(User user);
    
    /**
     * Valida un token de verificación.
     * @param token Valor del token a validar
     * @return EmailVerificationToken si es válido, lanza excepción si no
     */
    EmailVerificationToken validateToken(String token);
    
    /**
     * Marca un token como utilizado.
     * @param token El token a validar y marcar
     * @return true si se marcó correctamente, false si el token ya estaba usado
     */
    boolean confirmToken(String token);
    
    /**
     * Obtiene el token de verificación activo de un usuario.
     * @param userId ID del usuario
     * @return Token si existe, lanza excepción si no
     */
    EmailVerificationToken getActiveTokenByUserId(UUID userId);
    
    /**
     * Elimina todos los tokens de verificación de un usuario.
     * @param userId ID del usuario
     */
    void deleteTokensByUserId(UUID userId);
}
