package com.codefactory.reservasmsauthservice.repository;

import com.codefactory.reservasmsauthservice.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio para LoginAttempt.
 * Gestiona operaciones CRUD y consultas de intentos de inicio de sesión.
 */
@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, UUID> {

    /**
     * Cuenta los intentos fallidos de un usuario en los últimos N minutos.
     * @param userId ID del usuario
     * @param since Fecha y hora desde la cual contar
     * @return Cantidad de intentos fallidos
     */
    @Query("SELECT COUNT(la) FROM LoginAttempt la WHERE la.user.idUsuario = :userId AND la.exitoso = false AND la.createdAt > :since")
    long countFailedAttemptsAfter(@Param("userId") UUID userId, @Param("since") LocalDateTime since);

    /**
     * Obtiene los últimos intentos fallidos de un usuario.
     * @param userId ID del usuario
     * @return Lista de intentos fallidos
     */
    List<LoginAttempt> findByUser_IdUsuarioAndExitosoFalseOrderByCreatedAtDesc(UUID userId);

    /**
     * Elimina los intentos antiguos de un usuario.
     * @param userId ID del usuario
     * @param before Fecha límite
     */
    void deleteByUser_IdUsuarioAndCreatedAtBefore(UUID userId, LocalDateTime before);
}
