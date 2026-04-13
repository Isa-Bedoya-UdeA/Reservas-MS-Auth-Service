package com.codefactory.reservasmsauthservice.repository;

import com.codefactory.reservasmsauthservice.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para PasswordResetToken.
 * Gestiona operaciones CRUD y consultas de tokens de restablecimiento de contraseña.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    /**
     * Busca un token de restablecimiento válido por su valor.
     * @param token Valor del token
     * @return Optional con el token si existe, no está usado y no ha expirado
     */
    @Query("SELECT prt FROM PasswordResetToken prt WHERE prt.token = :token AND prt.usado = false AND prt.expiryDate > CURRENT_TIMESTAMP")
    Optional<PasswordResetToken> findValidByToken(@Param("token") String token);

    /**
     * Busca cualquier token de restablecimiento por su valor.
     * @param token Valor del token
     * @return Optional con el token si existe
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Busca tokens de restablecimiento no utilizados de un usuario.
     * @param userId ID del usuario
     * @return Lista de tokens no usados
     */
    java.util.List<PasswordResetToken> findByUser_IdUsuarioAndUsadoFalseOrderByCreatedAtDesc(UUID userId);

    /**
     * Elimina los tokens de restablecimiento expirados.
     * @param expiryDate Fecha límite
     */
    void deleteByExpiryDateBefore(LocalDateTime expiryDate);

    /**
     * Elimina todos los tokens de restablecimiento de un usuario.
     * @param userId ID del usuario
     */
    void deleteByUser_IdUsuario(UUID userId);
}
