package com.codefactory.reservasmsauthservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountLockedException Tests")
class AccountLockedExceptionTest {

    @Test
    @DisplayName("Exception creation")
    void exception_Created() {
        AccountLockedException ex = new AccountLockedException("Cuenta bloqueada");
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("Cuenta bloqueada");
    }
}