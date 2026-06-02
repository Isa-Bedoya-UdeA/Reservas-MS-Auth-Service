package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InvalidResetTokenException Tests")
class InvalidResetTokenExceptionTest {

    @Test
    @DisplayName("Constructor with message")
    void constructorWithMessage() {
        InvalidResetTokenException ex = new InvalidResetTokenException("Token de restablecimiento inválido o expirado");
        assertThat(ex.getMessage()).isEqualTo("Token de restablecimiento inválido o expirado");
    }

    @Test
    @DisplayName("Default constructor")
    void defaultConstructor() {
        InvalidResetTokenException ex = new InvalidResetTokenException();
        assertThat(ex.getMessage()).isEqualTo("El token de reset de contraseña es inválido, ha expirado o ya fue usado");
    }

    @Test
    @DisplayName("Inherits from BusinessException")
    void inheritsFromBusinessException() {
        InvalidResetTokenException ex = new InvalidResetTokenException("Test");
        assertThat(ex).isInstanceOf(BusinessException.class);
    }
}