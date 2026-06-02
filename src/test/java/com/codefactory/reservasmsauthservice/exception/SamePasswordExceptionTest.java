package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SamePasswordException Tests")
class SamePasswordExceptionTest {

    @Test
    @DisplayName("Constructor with message")
    void constructorWithMessage() {
        SamePasswordException ex = new SamePasswordException("La nueva contraseña no puede ser igual a la actual");
        assertThat(ex.getMessage()).isEqualTo("La nueva contraseña no puede ser igual a la actual");
    }

    @Test
    @DisplayName("Default constructor")
    void defaultConstructor() {
        SamePasswordException ex = new SamePasswordException();
        assertThat(ex.getMessage()).isEqualTo("La nueva contraseña no puede ser igual a la actual");
    }

    @Test
    @DisplayName("Inherits from BusinessException")
    void inheritsFromBusinessException() {
        SamePasswordException ex = new SamePasswordException("Test");
        assertThat(ex).isInstanceOf(BusinessException.class);
    }
}