package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.external.ExternalClientDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateClientRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.ClientResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Client;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.mapper.ClientMapper;
import com.codefactory.reservasmsauthservice.repository.ClientRepository;
import com.codefactory.reservasmsauthservice.service.UserAuthService;
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
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private UserAuthService userAuthService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "cliente@test.com";
    private static final String TEST_PASSWORD = "Password123";
    private static final String TEST_NAME = "Juan Pérez";

    private User testUser;
    private Client testClient;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setIdUsuario(TEST_USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setTipoUsuario(User.Role.CLIENTE);

        testClient = new Client();
        testClient.setIdUsuario(TEST_USER_ID);
        testClient.setEmail(TEST_EMAIL);
        testClient.setNombre(TEST_NAME);
        testClient.setTipoUsuario(User.Role.CLIENTE);
    }

    @Nested
    @DisplayName("createClient")
    class CreateClientTests {

        @Test
        @DisplayName("Debe crear cliente exitosamente")
        void createClient_Success() {
            CreateClientRequestDTO request = new CreateClientRequestDTO();
            request.setEmail(TEST_EMAIL);
            request.setPassword(TEST_PASSWORD);
            request.setNombre(TEST_NAME);
            request.setTelefono("1234567890");

            ClientResponseDTO expectedResponse = new ClientResponseDTO();
            expectedResponse.setIdUsuario(TEST_USER_ID);
            expectedResponse.setEmail(TEST_EMAIL);
            expectedResponse.setNombre(TEST_NAME);
            expectedResponse.setTipoUsuario("CLIENTE");

            when(clientMapper.toEntity(request)).thenReturn(testClient);
            when(clientMapper.toDto(any(Client.class))).thenReturn(expectedResponse);
            when(clientRepository.save(any(Client.class))).thenReturn(testClient);

            ClientResponseDTO result = clientService.createClient(request);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
            assertThat(result.getNombre()).isEqualTo(TEST_NAME);
            verify(userAuthService).validateEmailAndPassword(TEST_EMAIL, TEST_PASSWORD);
            verify(userAuthService).encodePassword(TEST_PASSWORD);
            verify(clientRepository).save(any(Client.class));
        }
    }

    @Nested
    @DisplayName("getUserEntityByEmail")
    class GetUserEntityByEmailTests {

        @Test
        @DisplayName("Debe retornar usuario por email exitosamente")
        void getUserEntityByEmail_Success() {
            when(clientRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testClient));

            User result = clientService.getUserEntityByEmail(TEST_EMAIL);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe lanzar excepción si no encuentra usuario")
        void getUserEntityByEmail_NotFound() {
            when(clientRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> clientService.getUserEntityByEmail(TEST_EMAIL))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getExternalClientById")
    class GetExternalClientByIdTests {

        @Test
        @DisplayName("Debe retornar ExternalClientDTO por ID")
        void getExternalClientById_Success() {
            ExternalClientDTO expected = ExternalClientDTO.builder()
                    .nombre(TEST_NAME)
                    .email(TEST_EMAIL)
                    .activo(true)
                    .build();

            when(clientRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testClient));

            ExternalClientDTO result = clientService.getExternalClientById(TEST_USER_ID);

            assertThat(result).isNotNull();
            assertThat(result.getNombre()).isEqualTo(TEST_NAME);
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe lanzar excepción si no encuentra cliente")
        void getExternalClientById_NotFound() {
            when(clientRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> clientService.getExternalClientById(TEST_USER_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}