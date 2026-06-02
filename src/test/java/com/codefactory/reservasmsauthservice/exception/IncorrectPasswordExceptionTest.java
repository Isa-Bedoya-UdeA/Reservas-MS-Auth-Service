package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("IncorrectPasswordException Tests")
class IncorrectPasswordExceptionTest {

    @Test
    @DisplayName("Constructor with message")
    void constructorWithMessage() {
        IncorrectPasswordException ex = new IncorrectPasswordException("La contraseña actual es incorrecta");
        assertThat(ex.getMessage()).isEqualTo("La contraseña actual es incorrecta");
    }

    @Test
    @DisplayName("Default constructor")
    void defaultConstructor() {
        IncorrectPasswordException ex = new IncorrectPasswordException();
        assertThat(ex.getMessage()).isEqualTo("La contraseña actual es incorrecta");
    }

    @Test
    @DisplayName("Inherits from BusinessException")
    void inheritsFromBusinessException() {
        IncorrectPasswordException ex = new IncorrectPasswordException("Test");
        assertThat(ex).isInstanceOf(BusinessException.class);
    }
}