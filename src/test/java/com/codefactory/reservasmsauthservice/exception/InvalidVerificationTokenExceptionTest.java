package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InvalidVerificationTokenException Tests")
class InvalidVerificationTokenExceptionTest {

    @Test
    @DisplayName("Constructor with message")
    void constructorWithMessage() {
        InvalidVerificationTokenException ex = new InvalidVerificationTokenException("Token de verificación inválido");
        assertThat(ex.getMessage()).isEqualTo("Token de verificación inválido");
        assertThat(ex.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Constructor with message and expired flag")
    void constructorWithExpiredFlag() {
        InvalidVerificationTokenException ex = new InvalidVerificationTokenException("Token expirado", true);
        assertThat(ex.getMessage()).isEqualTo("Token expirado");
        assertThat(ex.isExpired()).isTrue();
    }

    @Test
    @DisplayName("isExpired returns false by default")
    void isExpiredFalseByDefault() {
        InvalidVerificationTokenException ex = new InvalidVerificationTokenException("Test");
        assertThat(ex.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Inherits from RuntimeException")
    void inheritsFromRuntimeException() {
        InvalidVerificationTokenException ex = new InvalidVerificationTokenException("Test");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }
}