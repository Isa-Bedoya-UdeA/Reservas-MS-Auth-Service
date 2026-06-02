package com.codefactory.reservasmsauthservice.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PasswordResetToken Entity Tests")
class PasswordResetTokenTest {

    @Test
    @DisplayName("Builder creates entity with all fields")
    void builder_CreatesEntity() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusHours(24);
        
        PasswordResetToken token = PasswordResetToken.builder()
                .idResetToken(UUID.randomUUID())
                .token("reset-token-123")
                .expiryDate(expiry)
                .usado(false)
                .ipAddress("192.168.1.1")
                .fechaCreacion(now)
                .createdAt(now)
                .build();

        assertThat(token.getIdResetToken()).isNotNull();
        assertThat(token.getToken()).isEqualTo("reset-token-123");
        assertThat(token.getExpiryDate()).isEqualTo(expiry);
        assertThat(token.getUsado()).isFalse();
        assertThat(token.getIpAddress()).isEqualTo("192.168.1.1");
        assertThat(token.getFechaCreacion()).isEqualTo(now);
        assertThat(token.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("onCreate sets defaults when null")
    void onCreate_SetsDefaults() {
        PasswordResetToken token = PasswordResetToken.builder()
                .token("test-token")
                .ipAddress("10.0.0.1")
                .build();

        token.onCreate();

        assertThat(token.getExpiryDate()).isNotNull();
        assertThat(token.getFechaCreacion()).isNotNull();
        assertThat(token.getCreatedAt()).isNotNull();
        assertThat(token.getUsado()).isFalse();
    }

    @Test
    @DisplayName("onCreate preserves existing values")
    void onCreate_PreservesExisting() {
        LocalDateTime existingExpiry = LocalDateTime.of(2025, 6, 1, 12, 0);
        LocalDateTime existingCreated = LocalDateTime.of(2025, 5, 31, 12, 0);
        
        PasswordResetToken token = PasswordResetToken.builder()
                .token("test-token")
                .ipAddress("10.0.0.1")
                .expiryDate(existingExpiry)
                .fechaCreacion(existingCreated)
                .usado(true)
                .build();

        token.onCreate();

        assertThat(token.getExpiryDate()).isEqualTo(existingExpiry);
        assertThat(token.getFechaCreacion()).isEqualTo(existingCreated);
        assertThat(token.getUsado()).isTrue();
    }

    @Test
    @DisplayName("Token marked as used")
    void token_MarkedAsUsed() {
        PasswordResetToken token = PasswordResetToken.builder()
                .token("used-token")
                .usado(true)
                .fechaUso(LocalDateTime.now())
                .ipAddress("10.0.0.1")
                .build();

        assertThat(token.getUsado()).isTrue();
        assertThat(token.getFechaUso()).isNotNull();
    }

    @Test
    @DisplayName("Token not yet used")
    void token_NotYetUsed() {
        PasswordResetToken token = PasswordResetToken.builder()
                .token("unused-token")
                .usado(false)
                .ipAddress("10.0.0.1")
                .build();

        assertThat(token.getUsado()).isFalse();
        assertThat(token.getFechaUso()).isNull();
    }
}