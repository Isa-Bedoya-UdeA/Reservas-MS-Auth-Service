package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.BusinessException;
import com.codefactory.reservasmsauthservice.exception.EmailAlreadyExistsException;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import com.codefactory.reservasmsauthservice.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password123";

    @Nested
    @DisplayName("validateEmailAndPassword")
    class ValidateEmailAndPasswordTests {

        @Test
        @DisplayName("Debe pasar validación si email no existe")
        void validateEmailAndPassword_EmailNotExists_Success() {
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);

            userAuthService.validateEmailAndPassword(TEST_EMAIL, TEST_PASSWORD);

            verify(userRepository).existsByEmail(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe lanzar excepción si email ya existe")
        void validateEmailAndPassword_EmailExists_ThrowsException() {
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

            assertThatThrownBy(() -> userAuthService.validateEmailAndPassword(TEST_EMAIL, TEST_PASSWORD))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessageContaining("ya está en uso");
        }
    }

    @Nested
    @DisplayName("encodePassword")
    class EncodePasswordTests {

        @Test
        @DisplayName("Debe codificar contraseña exitosamente")
        void encodePassword_Success() {
            when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("hashedPassword123");

            String result = userAuthService.encodePassword(TEST_PASSWORD);

            assertThat(result).isEqualTo("hashedPassword123");
            verify(passwordEncoder).encode(TEST_PASSWORD);
        }
    }

    @Nested
    @DisplayName("validateEmailVerification")
    class ValidateEmailVerificationTests {

        @Test
        @DisplayName("Debe pasar validación si email está verificado")
        void validateEmailVerification_Verified_Success() {
            User user = new User();
            user.setEmailVerificado(true);

            userAuthService.validateEmailVerification(user);

            // No exception thrown
        }

        @Test
        @DisplayName("Debe lanzar excepción si email no está verificado")
        void validateEmailVerification_NotVerified_ThrowsException() {
            User user = new User();
            user.setEmailVerificado(false);

            assertThatThrownBy(() -> userAuthService.validateEmailVerification(user))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("no ha sido verificado");
        }

@Test
        @DisplayName("No debe lanzar cuando emailVerificado es null")
        void validateEmailVerification_Null_NoException() {
            User user = new User();
            user.setEmailVerificado(null);

            assertThatNoException().isThrownBy(() -> userAuthService.validateEmailVerification(user));
        }
    }
}