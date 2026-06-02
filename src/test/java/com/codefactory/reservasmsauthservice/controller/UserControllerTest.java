package com.codefactory.reservasmsauthservice.controller;

import com.codefactory.reservasmsauthservice.dto.external.ExternalClientDTO;
import com.codefactory.reservasmsauthservice.dto.external.ExternalProviderDTO;
import com.codefactory.reservasmsauthservice.exception.GlobalExceptionHandler;
import com.codefactory.reservasmsauthservice.security.JwtAuthenticationEntryPoint;
import com.codefactory.reservasmsauthservice.security.JwtAuthenticationFilter;
import com.codefactory.reservasmsauthservice.security.JwtService;
import com.codefactory.reservasmsauthservice.service.ClientService;
import com.codefactory.reservasmsauthservice.service.ProviderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ProviderService providerService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final UUID TEST_CLIENT_ID = UUID.randomUUID();
    private static final UUID TEST_PROVIDER_ID = UUID.randomUUID();

    @Nested
    @DisplayName("GET /api/users/clients/{id}")
    class GetClientByIdTests {

        @Test
        @DisplayName("Debe retornar cliente por ID")
        void getClientById_ReturnsClient() throws Exception {
            ExternalClientDTO client = new ExternalClientDTO();
            client.setNombre("Juan Pérez");
            client.setEmail("juan@test.com");
            client.setActivo(true);

            when(clientService.getExternalClientById(TEST_CLIENT_ID)).thenReturn(client);

            mockMvc.perform(get("/api/users/clients/" + TEST_CLIENT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                    .andExpect(jsonPath("$.email").value("juan@test.com"));
        }
    }

    @Nested
    @DisplayName("GET /api/users/providers/{id}")
    class GetProviderByIdTests {

        @Test
        @DisplayName("Debe retornar proveedor por ID")
        void getProviderById_ReturnsProvider() throws Exception {
            ExternalProviderDTO provider = new ExternalProviderDTO();
            provider.setNombreComercial("Barbería Test");
            provider.setEmail("barberia@test.com");
            provider.setActivo(true);

            when(providerService.getExternalProviderById(TEST_PROVIDER_ID)).thenReturn(provider);

            mockMvc.perform(get("/api/users/providers/" + TEST_PROVIDER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombreComercial").value("Barbería Test"));
        }
    }
}