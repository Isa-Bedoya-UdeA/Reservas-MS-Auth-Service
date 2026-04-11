package com.codefactory.reservasmsauthservice.repository;

import com.codefactory.reservasmsauthservice.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio para EmailVerificationToken.
 * Gestiona operaciones CRUD y consultas específicas de tokens de verificación.
 */
@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    
    /**
     * Busca un token de verificación no expirado y no utilizado por su valor.
     * @param token Valor del token
     * @return Optional con el token si existe y es válido
     */
    @Query("SELECT evt FROM EmailVerificationToken evt WHERE evt.token = :token AND evt.used = false AND evt.expiryDate > CURRENT_TIMESTAMP")
    Optional<EmailVerificationToken> findValidByToken(@Param("token") String token);
    
    /**
     * Busca cualquier token de verificación por su valor.
     * @param token Valor del token
     * @return Optional con el token si existe
     */
    Optional<EmailVerificationToken> findByToken(String token);
    
    /**
     * Busca el token de verificación activo de un usuario.
     * @param userId ID del usuario
     * @return Optional con el token no utilizado si existe
     */
    Optional<EmailVerificationToken> findByUser_IdUsuarioAndUsedFalse(Long userId);
    
    /**
     * Elimina todos los tokens de verificación de un usuario.
     * @param userId ID del usuario
     */
    void deleteByUser_IdUsuario(Long userId);
    
    /**
     * Elimina tokens expirados y no utilizados.
     * @param expiryDate Fecha límite de expiración
     */
    void deleteByExpiryDateBeforeAndUsedFalse(LocalDateTime expiryDate);
}
