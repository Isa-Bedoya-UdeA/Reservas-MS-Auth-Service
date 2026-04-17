package com.codefactory.reservasmsauthservice.controller;

import com.codefactory.reservasmsauthservice.dto.request.VerifyEmailRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.VerificationResponseDTO;
import com.codefactory.reservasmsauthservice.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de verificación de email.
 * Maneja los endpoints para verificar emails y reenviar tokens de verificación.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Verificación", description = "Endpoints para verificación de email y reenvío de tokens")
public class VerificationController {

    private final VerificationService verificationService;

    /**
     * Verifica el email del usuario usando el token recibido.
     * 
     * @param request DTO con el token de verificación
     * @return Confirmación de la verificación exitosa
     */
    @PostMapping("/verify-email")
    @Operation(
        summary = "Verificar email",
        description = "Verifica el email del usuario usando el token recibido por email tras el registro.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Token de verificación",
            required = true,
            content = @Content(schema = @Schema(implementation = VerifyEmailRequestDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email verificado exitosamente", content = @Content(schema = @Schema(implementation = VerificationResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Token inválido o expirado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @SecurityRequirements
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequestDTO request) {
        VerificationResponseDTO response = verificationService.verifyEmail(request.getToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Reenvía un nuevo token de verificación al email del usuario.
     * Útil si el token anterior ha expirado.
     * 
     * @param email Email del usuario
     * @return Confirmación del reenvío
     */
    @PostMapping("/resend-verification-email")
    @Operation(
        summary = "Reenviar email de verificación",
        description = "Envía un nuevo token de verificación al email del usuario. Útil si el token anterior ha expirado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email de verificación reenviado exitosamente", content = @Content(schema = @Schema(implementation = VerificationResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Email ya verificado")
    })
    @SecurityRequirements
    public ResponseEntity<?> resendVerificationEmail(
        @Parameter(description = "Email del usuario", required = true, example = "usuario@ejemplo.com")
        @RequestParam String email) {
        VerificationResponseDTO response = verificationService.resendVerificationToken(email);
        return ResponseEntity.ok(response);
    }
}
