package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ResourceNotFoundException Tests")
class ResourceNotFoundExceptionAuthTest {

    @Test
    @DisplayName("Exception creation")
    void exception_Created() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("Resource not found");
    }
}