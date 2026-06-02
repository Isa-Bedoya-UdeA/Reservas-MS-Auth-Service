package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.response.VerificationResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Client;
import com.codefactory.reservasmsauthservice.entity.EmailVerificationToken;
import com.codefactory.reservasmsauthservice.entity.Provider;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.InvalidVerificationTokenException;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.repository.ClientRepository;
import com.codefactory.reservasmsauthservice.repository.EmailVerificationTokenRepository;
import com.codefactory.reservasmsauthservice.repository.ProviderRepository;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import com.codefactory.reservasmsauthservice.service.EmailService;
import com.codefactory.reservasmsauthservice.service.EmailVerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationServiceImplTest {

    @Mock
    private EmailVerificationTokenService emailVerificationTokenService;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private VerificationServiceImpl verificationService;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "test@example.com";
    private static final String VALID_TOKEN = "valid-token-123";

    private User testUser;
    private EmailVerificationToken testToken;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setIdUsuario(TEST_USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setTipoUsuario(User.Role.CLIENTE);
        testUser.setEmailVerificado(false);

        testToken = new EmailVerificationToken();
        testToken.setUser(testUser);
        testToken.setToken(VALID_TOKEN);
    }

    @Nested
    @DisplayName("verifyEmail")
    class VerifyEmailTests {

        @Test
        @DisplayName("Debe verificar email exitosamente")
        void verifyEmail_Success() {
            when(emailVerificationTokenService.validateToken(VALID_TOKEN)).thenReturn(testToken);
            when(emailVerificationTokenService.confirmToken(VALID_TOKEN)).thenReturn(true);
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            VerificationResponseDTO result = verificationService.verifyEmail(VALID_TOKEN);

            assertThat(result).isNotNull();
            assertThat(result.getSuccess()).isTrue();
            assertThat(result.getUserId()).isEqualTo(TEST_USER_ID);
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
            verify(userRepository).save(any(User.class));
            verify(emailVerificationTokenService).confirmToken(VALID_TOKEN);
        }

        @Test
        @DisplayName("Debe lanzar excepción para token inválido")
        void verifyEmail_InvalidToken_ThrowsException() {
            when(emailVerificationTokenService.validateToken("invalid-token"))
                    .thenThrow(new InvalidVerificationTokenException("Token inválido", true));

            assertThatThrownBy(() -> verificationService.verifyEmail("invalid-token"))
                    .isInstanceOf(InvalidVerificationTokenException.class);
        }
    }

    @Nested
    @DisplayName("resendVerificationToken")
    class ResendVerificationTokenTests {

        @Test
        @DisplayName("Debe reenviar token exitosamente para cliente")
        void resendVerificationToken_Client_Success() {
            testUser.setTipoUsuario(User.Role.CLIENTE);
            Client client = new Client();
            client.setNombre("Juan Pérez");

            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
            doNothing().when(emailVerificationTokenRepository).deleteByUser_IdUsuario(TEST_USER_ID);
            when(emailVerificationTokenService.generateToken(testUser)).thenReturn("new-token");
            when(clientRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(client));

            VerificationResponseDTO result = verificationService.resendVerificationToken(TEST_EMAIL);

            assertThat(result).isNotNull();
            assertThat(result.getSuccess()).isTrue();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
            verify(emailService).sendVerificationEmail(TEST_EMAIL, "Juan Pérez", "new-token");
        }

        @Test
        @DisplayName("Debe reenviar token exitosamente para proveedor")
        void resendVerificationToken_Provider_Success() {
            testUser.setTipoUsuario(User.Role.PROVEEDOR);
            Provider provider = new Provider();
            provider.setNombreComercial("Barbería Test");

            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
            doNothing().when(emailVerificationTokenRepository).deleteByUser_IdUsuario(TEST_USER_ID);
            when(emailVerificationTokenService.generateToken(testUser)).thenReturn("new-token");
            when(providerRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(provider));

            VerificationResponseDTO result = verificationService.resendVerificationToken(TEST_EMAIL);

            assertThat(result).isNotNull();
            assertThat(result.getSuccess()).isTrue();
            verify(emailService).sendVerificationEmail(TEST_EMAIL, "Barbería Test", "new-token");
        }

        @Test
        @DisplayName("Debe retornar success false si email ya está verificado")
        void resendVerificationToken_AlreadyVerified_ReturnsFalse() {
            testUser.setEmailVerificado(true);
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

            VerificationResponseDTO result = verificationService.resendVerificationToken(TEST_EMAIL);

            assertThat(result).isNotNull();
            assertThat(result.getSuccess()).isFalse();
            assertThat(result.getMessage()).contains("ya ha sido verificado");
            verify(emailVerificationTokenService, never()).generateToken(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción si usuario no existe")
        void resendVerificationToken_UserNotFound_ThrowsException() {
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> verificationService.resendVerificationToken(TEST_EMAIL))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}