package com.codefactory.reservasmsauthservice.controller;

import com.codefactory.reservasmsauthservice.dto.request.CreateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.UpdateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.AdminResponseDTO;
import com.codefactory.reservasmsauthservice.exception.GlobalExceptionHandler;
import com.codefactory.reservasmsauthservice.security.JwtAuthenticationEntryPoint;
import com.codefactory.reservasmsauthservice.security.JwtAuthenticationFilter;
import com.codefactory.reservasmsauthservice.security.JwtService;
import com.codefactory.reservasmsauthservice.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final UUID TEST_ADMIN_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "admin@test.com";
    private static final String TEST_PASSWORD = "Password123";
    private static final String TEST_NAME = "Admin Test";

    @Nested
    @DisplayName("POST /api/auth/admins/initialize")
    class InitializeFirstAdminTests {

        @Test
        @DisplayName("Debe inicializar primer admin exitosamente")
        void initializeFirstAdmin_Success() throws Exception {
            CreateAdminRequestDTO request = new CreateAdminRequestDTO();
            request.setEmail(TEST_EMAIL);
            request.setPassword(TEST_PASSWORD);
            request.setNombreCompleto(TEST_NAME);
            request.setTelefono("1234567890");
            request.setCodigoEmpleado("ADM001");

            AdminResponseDTO response = new AdminResponseDTO();
            response.setIdUsuario(TEST_ADMIN_ID);
            response.setEmail(TEST_EMAIL);
            response.setNombreCompleto(TEST_NAME);

            when(adminService.initializeFirstAdmin(any(CreateAdminRequestDTO.class))).thenReturn(response);

            mockMvc.perform(post("/api/auth/admins/initialize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idUsuario").value(TEST_ADMIN_ID.toString()))
                    .andExpect(jsonPath("$.email").value(TEST_EMAIL));
        }

        @Test
        @DisplayName("Debe retornar 400 para email inválido")
        void initializeFirstAdmin_InvalidEmail() throws Exception {
            CreateAdminRequestDTO request = new CreateAdminRequestDTO();
            request.setEmail("not-an-email");
            request.setPassword(TEST_PASSWORD);
            request.setNombreCompleto(TEST_NAME);
            request.setTelefono("1234567890");

            mockMvc.perform(post("/api/auth/admins/initialize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/admins")
    class CreateAdminTests {

        @Test
        @DisplayName("Debe crear admin exitosamente")
        void createAdmin_Success() throws Exception {
            CreateAdminRequestDTO request = new CreateAdminRequestDTO();
            request.setEmail(TEST_EMAIL);
            request.setPassword(TEST_PASSWORD);
            request.setNombreCompleto(TEST_NAME);
            request.setTelefono("1234567890");
            request.setCodigoEmpleado("ADM002");

            AdminResponseDTO response = new AdminResponseDTO();
            response.setIdUsuario(TEST_ADMIN_ID);
            response.setEmail(TEST_EMAIL);
            response.setNombreCompleto(TEST_NAME);

            when(adminService.createAdmin(any(CreateAdminRequestDTO.class), any(UUID.class))).thenReturn(response);

            mockMvc.perform(post("/api/auth/admins")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("creadoPor", UUID.randomUUID().toString())
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("GET /api/auth/admins")
    class GetAllAdminsTests {

        @Test
        @DisplayName("Debe retornar lista de admins")
        void getAllAdmins_ReturnsList() throws Exception {
            AdminResponseDTO admin = new AdminResponseDTO();
            admin.setIdUsuario(TEST_ADMIN_ID);
            admin.setEmail(TEST_EMAIL);
            admin.setNombreCompleto(TEST_NAME);

            when(adminService.getAllAdmins()).thenReturn(List.of(admin));

            mockMvc.perform(get("/api/auth/admins"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/auth/admins/{id}")
    class GetAdminByIdTests {

        @Test
        @DisplayName("Debe retornar admin por ID")
        void getAdminById_ReturnsAdmin() throws Exception {
            AdminResponseDTO response = new AdminResponseDTO();
            response.setIdUsuario(TEST_ADMIN_ID);
            response.setEmail(TEST_EMAIL);
            response.setNombreCompleto(TEST_NAME);

            when(adminService.getAdminById(TEST_ADMIN_ID)).thenReturn(response);

            mockMvc.perform(get("/api/auth/admins/" + TEST_ADMIN_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idUsuario").value(TEST_ADMIN_ID.toString()));
        }
    }

    @Nested
    @DisplayName("PUT /api/auth/admins/{id}")
    class UpdateAdminTests {

        @Test
        @DisplayName("Debe actualizar admin exitosamente")
        void updateAdmin_Success() throws Exception {
            UpdateAdminRequestDTO request = new UpdateAdminRequestDTO();
            request.setNombreCompleto("Nuevo Nombre");
            request.setTelefono("9999999999");

            AdminResponseDTO response = new AdminResponseDTO();
            response.setIdUsuario(TEST_ADMIN_ID);
            response.setEmail(TEST_EMAIL);
            response.setNombreCompleto("Nuevo Nombre");

            when(adminService.updateAdmin(any(UUID.class), any(UpdateAdminRequestDTO.class))).thenReturn(response);

            mockMvc.perform(put("/api/auth/admins/" + TEST_ADMIN_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("DELETE /api/auth/admins/{id}")
    class DeactivateAdminTests {

        @Test
        @DisplayName("Debe desactivar admin exitosamente")
        void deactivateAdmin_Success() throws Exception {
            doNothing().when(adminService).deactivateAdmin(TEST_ADMIN_ID);

            mockMvc.perform(delete("/api/auth/admins/" + TEST_ADMIN_ID))
                    .andExpect(status().isNoContent());
        }
    }
}