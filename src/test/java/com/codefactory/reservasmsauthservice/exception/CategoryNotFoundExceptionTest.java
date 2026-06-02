package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CategoryNotFoundException Tests")
class CategoryNotFoundExceptionTest {

    @Test
    @DisplayName("Constructor with message")
    void constructorWithMessage() {
        CategoryNotFoundException ex = new CategoryNotFoundException("Categoría no encontrada con ID: 123");
        assertThat(ex.getMessage()).isEqualTo("Categoría no encontrada con ID: 123");
    }

    @Test
    @DisplayName("Inherits from RuntimeException")
    void inheritsFromRuntimeException() {
        CategoryNotFoundException ex = new CategoryNotFoundException("Test");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }
}