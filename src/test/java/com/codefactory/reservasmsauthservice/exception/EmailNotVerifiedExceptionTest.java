package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EmailNotVerifiedException Tests")
class EmailNotVerifiedExceptionTest {

    @Test
    @DisplayName("Exception creation")
    void exception_Created() {
        EmailNotVerifiedException ex = new EmailNotVerifiedException("Email no verificado");
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("Email no verificado");
    }
}