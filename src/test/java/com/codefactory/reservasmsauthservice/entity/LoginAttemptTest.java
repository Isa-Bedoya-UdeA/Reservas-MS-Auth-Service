package com.codefactory.reservasmsauthservice.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginAttempt Entity Tests")
class LoginAttemptTest {

    @Test
    @DisplayName("Builder creates entity with all fields")
    void builder_CreatesEntity() {
        LocalDateTime now = LocalDateTime.now();
        
        LoginAttempt attempt = LoginAttempt.builder()
                .idAttempt(UUID.randomUUID())
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .exitoso(true)
                .errorMessage("Success")
                .fechaHora(now)
                .createdAt(now)
                .build();

        assertThat(attempt.getIdAttempt()).isNotNull();
        assertThat(attempt.getIpAddress()).isEqualTo("192.168.1.1");
        assertThat(attempt.getUserAgent()).isEqualTo("Mozilla/5.0");
        assertThat(attempt.getExitoso()).isTrue();
        assertThat(attempt.getErrorMessage()).isEqualTo("Success");
        assertThat(attempt.getFechaHora()).isEqualTo(now);
        assertThat(attempt.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("onCreate sets defaults when null")
    void onCreate_SetsDefaults() {
        LoginAttempt attempt = LoginAttempt.builder()
                .ipAddress("10.0.0.1")
                .build();

        attempt.onCreate();

        assertThat(attempt.getFechaHora()).isNotNull();
        assertThat(attempt.getCreatedAt()).isNotNull();
        assertThat(attempt.getExitoso()).isFalse();
    }

    @Test
    @DisplayName("onCreate preserves existing values")
    void onCreate_PreservesExisting() {
        LocalDateTime existingFecha = LocalDateTime.of(2024, 1, 15, 10, 30);
        LocalDateTime existingCreated = LocalDateTime.of(2024, 1, 15, 10, 35);
        
        LoginAttempt attempt = LoginAttempt.builder()
                .ipAddress("10.0.0.1")
                .fechaHora(existingFecha)
                .createdAt(existingCreated)
                .exitoso(true)
                .build();

        attempt.onCreate();

        assertThat(attempt.getFechaHora()).isEqualTo(existingFecha);
        assertThat(attempt.getCreatedAt()).isEqualTo(existingCreated);
        assertThat(attempt.getExitoso()).isTrue();
    }

    @Test
    @DisplayName("Failed login attempt")
    void failedLoginAttempt() {
        LoginAttempt attempt = LoginAttempt.builder()
                .ipAddress("192.168.1.100")
                .exitoso(false)
                .errorMessage("Invalid credentials")
                .build();

        assertThat(attempt.getExitoso()).isFalse();
        assertThat(attempt.getErrorMessage()).isEqualTo("Invalid credentials");
    }

    @Test
    @DisplayName("Successful login attempt")
    void successfulLoginAttempt() {
        LoginAttempt attempt = LoginAttempt.builder()
                .ipAddress("192.168.1.100")
                .exitoso(true)
                .build();

        assertThat(attempt.getExitoso()).isTrue();
    }
}