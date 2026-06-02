package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.entity.EmailVerificationToken;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.InvalidVerificationTokenException;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.repository.EmailVerificationTokenRepository;
import com.codefactory.reservasmsauthservice.service.EmailVerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailVerificationTokenServiceImplTest {

    @Mock
    private EmailVerificationTokenRepository tokenRepository;

    @InjectMocks
    private EmailVerificationTokenServiceImpl emailVerificationTokenService;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String VALID_TOKEN = "valid-token-123";

    private User testUser;
    private EmailVerificationToken testToken;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setIdUsuario(TEST_USER_ID);
        testUser.setEmail("test@example.com");

        testToken = new EmailVerificationToken();
        testToken.setUser(testUser);
        testToken.setToken(VALID_TOKEN);
        testToken.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        testToken.setUsado(false);
    }

    @Nested
    @DisplayName("generateToken")
    class GenerateTokenTests {

        @Test
        @DisplayName("Debe generar token exitosamente")
        void generateToken_Success() {
            doNothing().when(tokenRepository).deleteByUser_IdUsuario(TEST_USER_ID);
            when(tokenRepository.save(any(EmailVerificationToken.class))).thenAnswer(inv -> inv.getArgument(0));

            String result = emailVerificationTokenService.generateToken(testUser);

            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            verify(tokenRepository).save(any(EmailVerificationToken.class));
            verify(tokenRepository).deleteByUser_IdUsuario(TEST_USER_ID);
        }
    }

    @Nested
    @DisplayName("validateToken")
    class ValidateTokenTests {

        @Test
        @DisplayName("Debe validar token exitosamente")
        void validateToken_ValidToken_Success() {
            when(tokenRepository.findValidByToken(VALID_TOKEN)).thenReturn(Optional.of(testToken));

            EmailVerificationToken result = emailVerificationTokenService.validateToken(VALID_TOKEN);

            assertThat(result).isNotNull();
            assertThat(result.getToken()).isEqualTo(VALID_TOKEN);
        }

        @Test
        @DisplayName("Debe lanzar excepción para token no encontrado")
        void validateToken_NotFound_ThrowsException() {
            when(tokenRepository.findValidByToken("invalid-token")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emailVerificationTokenService.validateToken("invalid-token"))
                    .isInstanceOf(InvalidVerificationTokenException.class);
        }
    }

    @Nested
    @DisplayName("confirmToken")
    class ConfirmTokenTests {

        @Test
        @DisplayName("Debe confirmar token exitosamente")
        void confirmToken_Success() {
            when(tokenRepository.findValidByToken(VALID_TOKEN)).thenReturn(Optional.of(testToken));
            when(tokenRepository.save(any(EmailVerificationToken.class))).thenAnswer(inv -> inv.getArgument(0));

            boolean result = emailVerificationTokenService.confirmToken(VALID_TOKEN);

            assertThat(result).isTrue();
            verify(tokenRepository).save(any(EmailVerificationToken.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción si token ya fue usado")
        void confirmToken_AlreadyUsed_ThrowsException() {
            testToken.setUsado(true);
            when(tokenRepository.findValidByToken(VALID_TOKEN)).thenReturn(Optional.of(testToken));

            assertThatThrownBy(() -> emailVerificationTokenService.confirmToken(VALID_TOKEN))
                    .isInstanceOf(InvalidVerificationTokenException.class);
        }
    }

    @Nested
    @DisplayName("getActiveTokenByUserId")
    class GetActiveTokenByUserIdTests {

        @Test
        @DisplayName("Debe retornar token activo por user ID")
        void getActiveTokenByUserId_Success() {
            when(tokenRepository.findByUser_IdUsuarioAndUsadoFalse(TEST_USER_ID)).thenReturn(Optional.of(testToken));

            EmailVerificationToken result = emailVerificationTokenService.getActiveTokenByUserId(TEST_USER_ID);

            assertThat(result).isNotNull();
            assertThat(result.getUser().getIdUsuario()).isEqualTo(TEST_USER_ID);
        }

        @Test
        @DisplayName("Debe lanzar excepción si no hay token activo")
        void getActiveTokenByUserId_NotFound_ThrowsException() {
            when(tokenRepository.findByUser_IdUsuarioAndUsadoFalse(TEST_USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emailVerificationTokenService.getActiveTokenByUserId(TEST_USER_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteTokensByUserId")
    class DeleteTokensByUserIdTests {

        @Test
        @DisplayName("Debe eliminar tokens por user ID exitosamente")
        void deleteTokensByUserId_Success() {
            doNothing().when(tokenRepository).deleteByUser_IdUsuario(TEST_USER_ID);

            emailVerificationTokenService.deleteTokensByUserId(TEST_USER_ID);

            verify(tokenRepository).deleteByUser_IdUsuario(TEST_USER_ID);
        }
    }
}