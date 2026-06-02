package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EmailAlreadyExistsException Tests")
class EmailAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Exception creation")
    void exception_Created() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Email ya existe");
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("Email ya existe");
    }
}