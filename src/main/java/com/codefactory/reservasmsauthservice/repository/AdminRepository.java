package com.codefactory.reservasmsauthservice.repository;

import com.codefactory.reservasmsauthservice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Admin> findByActivoTrue();
    Optional<Admin> findByIdUsuario(UUID idUsuario);
}
