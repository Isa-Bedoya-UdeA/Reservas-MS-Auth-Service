package com.codefactory.reservasmsauthservice.repository;

import com.codefactory.reservasmsauthservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para RefreshToken.
 * Gestiona operaciones CRUD y consultas de tokens de refresco.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /**
     * Busca un token de refresco válido por su valor.
     * @param token Valor del token
     * @return Optional con el token si existe y no está revocado
     */
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.token = :token AND rt.revocado = false AND rt.expiryDate > CURRENT_TIMESTAMP")
    Optional<RefreshToken> findValidByToken(@Param("token") String token);

    /**
     * Busca cualquier token de refresco por su valor.
     * @param token Valor del token
     * @return Optional con el token si existe
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Busca todos los tokens de refresco de un usuario.
     * @param userId ID del usuario
     * @return Lista de tokens del usuario
     */
    java.util.List<RefreshToken> findByUser_IdUsuario(UUID userId);

    /**
     * Busca tokens de refresco no revocados de un usuario.
     * @param userId ID del usuario
     * @return Lista de tokens no revocados
     */
    java.util.List<RefreshToken> findByUser_IdUsuarioAndRevocadoFalse(UUID userId);

    /**
     * Elimina los tokens de refresco expirados.
     * @param expiryDate Fecha límite
     */
    void deleteByExpiryDateBefore(LocalDateTime expiryDate);

    /**
     * Elimina todos los tokens de refresco de un usuario.
     * @param userId ID del usuario
     */
    void deleteByUser_IdUsuario(UUID userId);
}
