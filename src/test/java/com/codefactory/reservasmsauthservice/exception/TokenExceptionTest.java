package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TokenException Tests")
class TokenExceptionTest {

    @Test
    @DisplayName("Exception creation")
    void exception_Created() {
        TokenException ex = new TokenException("Token inválido");
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("Token inválido");
    }
}