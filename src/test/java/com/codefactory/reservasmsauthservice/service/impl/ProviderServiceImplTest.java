package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.client.CatalogClient;
import com.codefactory.reservasmsauthservice.dto.external.ExternalProviderDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.CategoryResponseDTO;
import com.codefactory.reservasmsauthservice.dto.response.ProviderResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Provider;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.CategoryNotFoundException;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.mapper.ProviderMapper;
import com.codefactory.reservasmsauthservice.repository.ProviderRepository;
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
class ProviderServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProviderMapper providerMapper;

    @Mock
    private UserAuthService userAuthService;

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private ProviderServiceImpl providerService;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final UUID TEST_CATEGORY_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "proveedor@test.com";
    private static final String TEST_PASSWORD = "Password123";
    private static final String TEST_COMMERCIAL_NAME = "Barbería Test";

    private User testUser;
    private Provider testProvider;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setIdUsuario(TEST_USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setTipoUsuario(User.Role.PROVEEDOR);

        testProvider = new Provider();
        testProvider.setIdUsuario(TEST_USER_ID);
        testProvider.setEmail(TEST_EMAIL);
        testProvider.setNombreComercial(TEST_COMMERCIAL_NAME);
        testProvider.setTipoUsuario(User.Role.PROVEEDOR);
        testProvider.setIdCategoria(TEST_CATEGORY_ID);
    }

    @Nested
    @DisplayName("createProvider")
    class CreateProviderTests {

        @Test
        @DisplayName("Debe crear proveedor exitosamente")
        void createProvider_Success() {
            CreateProviderRequestDTO request = new CreateProviderRequestDTO();
            request.setEmail(TEST_EMAIL);
            request.setPassword(TEST_PASSWORD);
            request.setNombreComercial(TEST_COMMERCIAL_NAME);
            request.setIdCategoria(TEST_CATEGORY_ID);
            request.setDireccion("Calle 123");
            request.setTelefonoContacto("1234567890");

            CategoryResponseDTO category = new CategoryResponseDTO();
            category.setActiva(true);

            ProviderResponseDTO expectedResponse = new ProviderResponseDTO();
            expectedResponse.setIdUsuario(TEST_USER_ID);
            expectedResponse.setEmail(TEST_EMAIL);
            expectedResponse.setNombreComercial(TEST_COMMERCIAL_NAME);
            expectedResponse.setTipoUsuario("PROVEEDOR");

            when(catalogClient.getCategoryById(TEST_CATEGORY_ID)).thenReturn(category);
            when(providerMapper.toEntity(request)).thenReturn(testProvider);
            when(providerMapper.toDto(any(Provider.class))).thenReturn(expectedResponse);
            when(providerRepository.save(any(Provider.class))).thenReturn(testProvider);

            ProviderResponseDTO result = providerService.createProvider(request);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
            assertThat(result.getNombreComercial()).isEqualTo(TEST_COMMERCIAL_NAME);
            verify(userAuthService).validateEmailAndPassword(TEST_EMAIL, TEST_PASSWORD);
            verify(userAuthService).encodePassword(TEST_PASSWORD);
            verify(providerRepository).save(any(Provider.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción si categoría no existe")
        void createProvider_CategoryNotFound() {
            CreateProviderRequestDTO request = new CreateProviderRequestDTO();
            request.setEmail(TEST_EMAIL);
            request.setPassword(TEST_PASSWORD);
            request.setNombreComercial(TEST_COMMERCIAL_NAME);
            request.setIdCategoria(TEST_CATEGORY_ID);

            when(catalogClient.getCategoryById(TEST_CATEGORY_ID)).thenReturn(null);

            assertThatThrownBy(() -> providerService.createProvider(request))
                    .isInstanceOf(CategoryNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getUserEntityByEmail")
    class GetUserEntityByEmailTests {

        @Test
        @DisplayName("Debe retornar usuario por email exitosamente")
        void getUserEntityByEmail_Success() {
            when(providerRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testProvider));

            User result = providerService.getUserEntityByEmail(TEST_EMAIL);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe lanzar excepción si no encuentra usuario")
        void getUserEntityByEmail_NotFound() {
            when(providerRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> providerService.getUserEntityByEmail(TEST_EMAIL))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getExternalProviderById")
    class GetExternalProviderByIdTests {

        @Test
        @DisplayName("Debe retornar ExternalProviderDTO por ID")
        void getExternalProviderById_Success() {
            ExternalProviderDTO expected = ExternalProviderDTO.builder()
                    .nombreComercial(TEST_COMMERCIAL_NAME)
                    .email(TEST_EMAIL)
                    .activo(true)
                    .build();

            when(providerRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testProvider));

            ExternalProviderDTO result = providerService.getExternalProviderById(TEST_USER_ID);

            assertThat(result).isNotNull();
            assertThat(result.getNombreComercial()).isEqualTo(TEST_COMMERCIAL_NAME);
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe lanzar excepción si no encuentra proveedor")
        void getExternalProviderById_NotFound() {
            when(providerRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> providerService.getExternalProviderById(TEST_USER_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}