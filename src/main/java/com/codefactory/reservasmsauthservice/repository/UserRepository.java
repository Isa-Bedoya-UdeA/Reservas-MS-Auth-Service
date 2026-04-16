package com.codefactory.reservasmsauthservice.repository;

import com.codefactory.reservasmsauthservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // Methods to get user names for email personalization
    @Query("SELECT c.nombre FROM Client c WHERE c.idUsuario = :userId")
    Optional<String> findClientNameByUserId(@Param("userId") UUID userId);

    @Query("SELECT p.nombreComercial FROM Provider p WHERE p.idUsuario = :userId")
    Optional<String> findProviderNameByUserId(@Param("userId") UUID userId);

    // Native queries to call RLS bypass functions (SECURITY DEFINER)
    // Using PERFORM instead of SELECT for void functions
    @Modifying
    @Transactional
    @Query(value = "DO $$ BEGIN PERFORM actualizar_intentos_fallidos(:userId, :intentos); END $$", nativeQuery = true)
    void updateFailedAttemptsBypass(@Param("userId") UUID userId, @Param("intentos") Integer intentos);

    @Modifying
    @Transactional
    @Query(value = "DO $$ BEGIN PERFORM resetear_intentos_fallidos(:userId); END $$", nativeQuery = true)
    void resetFailedAttemptsBypass(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query(value = "DO $$ BEGIN PERFORM bloquear_usuario(:userId, :bloqueadoHasta, :estado); END $$", nativeQuery = true)
    void lockUserBypass(@Param("userId") UUID userId, @Param("bloqueadoHasta") LocalDateTime bloqueadoHasta, @Param("estado") String estado);

    // Keep old methods for compatibility
    @Modifying
    @Query("UPDATE User u SET u.intentosFallidos = :intentos WHERE u.idUsuario = :userId")
    void updateFailedAttempts(@Param("userId") UUID userId, @Param("intentos") Integer intentos);

    @Modifying
    @Query("UPDATE User u SET u.intentosFallidos = 0, u.bloqueadoHasta = NULL WHERE u.idUsuario = :userId")
    void resetFailedAttempts(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE User u SET u.bloqueadoHasta = :bloqueadoHasta, u.estado = 'BLOQUEADO' WHERE u.idUsuario = :userId")
    void lockUser(@Param("userId") UUID userId, @Param("bloqueadoHasta") LocalDateTime bloqueadoHasta);
}
