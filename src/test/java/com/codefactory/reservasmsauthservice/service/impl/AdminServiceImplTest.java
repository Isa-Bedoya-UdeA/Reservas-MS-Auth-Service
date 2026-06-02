package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.request.CreateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.UpdateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.AdminResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Admin;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.mapper.AdminMapper;
import com.codefactory.reservasmsauthservice.repository.AdminRepository;
import com.codefactory.reservasmsauthservice.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AdminMapper adminMapper;

    @Mock
    private UserAuthService userAuthService;

    @InjectMocks
    private AdminServiceImpl adminService;

    private static final UUID TEST_ADMIN_ID = UUID.randomUUID();
    private static final UUID CREATED_BY_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "admin@test.com";
    private static final String TEST_PASSWORD = "Password123";
    private static final String TEST_NAME = "Admin Test";

    private Admin testAdmin;
    private AdminResponseDTO testAdminResponse;

    @BeforeEach
    void setUp() {
        testAdmin = new Admin();
        testAdmin.setIdUsuario(TEST_ADMIN_ID);
        testAdmin.setEmail(TEST_EMAIL);
        testAdmin.setNombreCompleto(TEST_NAME);
        testAdmin.setActivo(true);

        testAdminResponse = new AdminResponseDTO();
        testAdminResponse.setIdUsuario(TEST_ADMIN_ID);
        testAdminResponse.setEmail(TEST_EMAIL);
        testAdminResponse.setNombreCompleto(TEST_NAME);
    }

    @Nested
    @DisplayName("initializeFirstAdmin")
    class InitializeFirstAdminTests {

        @Test
        @DisplayName("Debe inicializar primer admin exitosamente")
        void initializeFirstAdmin_Success() {
            CreateAdminRequestDTO request = new CreateAdminRequestDTO();
            request.setEmail(TEST_EMAIL);
            request.setPassword(TEST_PASSWORD);
            request.setNombreCompleto(TEST_NAME);
            request.setTelefono("1234567890");
            request.setCodigoEmpleado("ADM001");

            when(adminRepository.findAll()).thenReturn(List.of());
            when(adminMapper.toEntity(request)).thenReturn(testAdmin);
            when(adminMapper.toDto(any(Admin.class))).thenReturn(testAdminResponse);
            when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

            AdminResponseDTO result = adminService.initializeFirstAdmin(request);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
            verify(adminRepository).save(any(Admin.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción si ya existen admins")
        void initializeFirstAdmin_AlreadyExists() {
            CreateAdminRequestDTO request = new CreateAdminRequestDTO();
            request.setEmail(TEST_EMAIL);
            request.setPassword(TEST_PASSWORD);

            when(adminRepository.findAll()).thenReturn(List.of(testAdmin));

            assertThatThrownBy(() -> adminService.initializeFirstAdmin(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Ya existen administradores");
        }
    }

    @Nested
    @DisplayName("createAdmin")
    class CreateAdminTests {

        @Test
        @DisplayName("Debe crear admin exitosamente")
        void createAdmin_Success() {
            CreateAdminRequestDTO request = new CreateAdminRequestDTO();
            request.setEmail(TEST_EMAIL);
            request.setPassword(TEST_PASSWORD);
            request.setNombreCompleto(TEST_NAME);
            request.setTelefono("1234567890");
            request.setCodigoEmpleado("ADM002");

            when(adminRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
            when(adminMapper.toEntity(request)).thenReturn(testAdmin);
            when(adminMapper.toDto(any(Admin.class))).thenReturn(testAdminResponse);
            when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

            AdminResponseDTO result = adminService.createAdmin(request, CREATED_BY_ID);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
            verify(adminRepository).save(any(Admin.class));
        }
    }

    @Nested
    @DisplayName("getAllAdmins")
    class GetAllAdminsTests {

        @Test
        @DisplayName("Debe retornar lista de admins")
        void getAllAdmins_ReturnsList() {
            when(adminRepository.findAll()).thenReturn(List.of(testAdmin));
            when(adminMapper.toDto(testAdmin)).thenReturn(testAdminResponse);

            List<AdminResponseDTO> result = adminService.getAllAdmins();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEmail()).isEqualTo(TEST_EMAIL);
        }
    }

    @Nested
    @DisplayName("getAdminById")
    class GetAdminByIdTests {

        @Test
        @DisplayName("Debe retornar admin por ID")
        void getAdminById_Success() {
            when(adminRepository.findById(TEST_ADMIN_ID)).thenReturn(Optional.of(testAdmin));
            when(adminMapper.toDto(testAdmin)).thenReturn(testAdminResponse);

            AdminResponseDTO result = adminService.getAdminById(TEST_ADMIN_ID);

            assertThat(result).isNotNull();
            assertThat(result.getIdUsuario()).isEqualTo(TEST_ADMIN_ID);
        }

        @Test
        @DisplayName("Debe lanzar excepción si no encuentra admin")
        void getAdminById_NotFound() {
            when(adminRepository.findById(TEST_ADMIN_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> adminService.getAdminById(TEST_ADMIN_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deactivateAdmin")
    class DeactivateAdminTests {

        @Test
        @DisplayName("Debe desactivar admin exitosamente")
        void deactivateAdmin_Success() {
            when(adminRepository.findById(TEST_ADMIN_ID)).thenReturn(Optional.of(testAdmin));
            when(adminRepository.save(any(Admin.class))).thenAnswer(inv -> inv.getArgument(0));

            adminService.deactivateAdmin(TEST_ADMIN_ID);

            verify(adminRepository).save(any(Admin.class));
            assertThat(testAdmin.getActivo()).isFalse();
        }
    }

    @Nested
    @DisplayName("activateAdmin")
    class ActivateAdminTests {

        @Test
        @DisplayName("Debe activar admin exitosamente")
        void activateAdmin_Success() {
            when(adminRepository.findById(TEST_ADMIN_ID)).thenReturn(Optional.of(testAdmin));
            when(adminRepository.save(any(Admin.class))).thenAnswer(inv -> inv.getArgument(0));

            adminService.activateAdmin(TEST_ADMIN_ID);

            verify(adminRepository).save(any(Admin.class));
            assertThat(testAdmin.getActivo()).isTrue();
        }
    }
}