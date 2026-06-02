package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InvalidPasswordException Tests")
class InvalidPasswordExceptionTest {

    @Test
    @DisplayName("Constructor with message")
    void constructorWithMessage() {
        InvalidPasswordException ex = new InvalidPasswordException("La contraseña no cumple los requisitos");
        assertThat(ex.getMessage()).isEqualTo("La contraseña no cumple los requisitos");
    }

    @Test
    @DisplayName("Default constructor")
    void defaultConstructor() {
        InvalidPasswordException ex = new InvalidPasswordException();
        assertThat(ex.getMessage()).isEqualTo("La contraseña no cumple con los requisitos de formato");
    }

    @Test
    @DisplayName("Inherits from BusinessException")
    void inheritsFromBusinessException() {
        InvalidPasswordException ex = new InvalidPasswordException("Test");
        assertThat(ex).isInstanceOf(BusinessException.class);
    }
}