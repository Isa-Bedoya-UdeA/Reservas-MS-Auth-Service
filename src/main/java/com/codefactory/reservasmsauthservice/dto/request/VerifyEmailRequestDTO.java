package com.codefactory.reservasmsauthservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para verificación de email.
 * Contiene el token de verificación enviado al email del usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyEmailRequestDTO {
    
    @NotBlank(message = "El token de verificación es requerido")
    private String token;
}
