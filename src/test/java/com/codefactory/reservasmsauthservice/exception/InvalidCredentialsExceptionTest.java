package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InvalidCredentialsException Tests")
class InvalidCredentialsExceptionTest {

    @Test
    @DisplayName("Exception creation with message")
    void exception_Created() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Credenciales inválidas");
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("Credenciales inválidas");
    }
}