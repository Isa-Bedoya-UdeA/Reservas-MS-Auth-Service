package com.codefactory.reservasmsauthservice.controller;

import com.codefactory.reservasmsauthservice.dto.request.VerifyEmailRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.VerificationResponseDTO;
import com.codefactory.reservasmsauthservice.exception.GlobalExceptionHandler;
import com.codefactory.reservasmsauthservice.security.JwtAuthenticationEntryPoint;
import com.codefactory.reservasmsauthservice.security.JwtAuthenticationFilter;
import com.codefactory.reservasmsauthservice.security.JwtService;
import com.codefactory.reservasmsauthservice.service.VerificationService;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VerificationController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VerificationService verificationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "test@example.com";

    @Nested
    @DisplayName("POST /api/auth/verify-email")
    class VerifyEmailTests {

        @Test
        @DisplayName("Debe verificar email exitosamente")
        void verifyEmail_Success() throws Exception {
            VerifyEmailRequestDTO request = new VerifyEmailRequestDTO();
            request.setToken("valid-token-123");

            VerificationResponseDTO response = VerificationResponseDTO.builder()
                    .success(true)
                    .message("¡Felicidades! Tu email ha sido verificado y tu cuenta está activa.")
                    .userId(TEST_USER_ID)
                    .email(TEST_EMAIL)
                    .build();

            when(verificationService.verifyEmail(anyString())).thenReturn(response);

            mockMvc.perform(post("/api/auth/verify-email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.userId").value(TEST_USER_ID.toString()))
                    .andExpect(jsonPath("$.email").value(TEST_EMAIL));
        }
    }

    @Nested
    @DisplayName("POST /api/auth/resend-verification-email")
    class ResendVerificationEmailTests {

        @Test
        @DisplayName("Debe reenviar email de verificación exitosamente")
        void resendVerificationEmail_Success() throws Exception {
            VerificationResponseDTO response = VerificationResponseDTO.builder()
                    .success(true)
                    .message("Se ha reenviado un nuevo token de verificación a tu email.")
                    .userId(TEST_USER_ID)
                    .email(TEST_EMAIL)
                    .build();

            when(verificationService.resendVerificationToken(anyString())).thenReturn(response);

            mockMvc.perform(post("/api/auth/resend-verification-email")
                            .param("email", TEST_EMAIL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").exists());
        }
    }
}