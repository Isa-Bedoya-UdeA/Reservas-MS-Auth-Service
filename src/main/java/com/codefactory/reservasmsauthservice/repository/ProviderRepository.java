package com.codefactory.reservasmsauthservice.repository;

import com.codefactory.reservasmsauthservice.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findByEmail(String email);
    boolean existsByEmail(String email);
}
