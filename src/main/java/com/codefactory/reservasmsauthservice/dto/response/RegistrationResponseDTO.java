package com.codefactory.reservasmsauthservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para registro de usuarios.
 * Contiene el token de verificación de email que debe ser confirmado
 * antes de poder iniciar sesión.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationResponseDTO {
    
    /**
     * Token de verificación de email.
     * Debe enviarse por email al usuario y ser confirmado.
     */
    private String verificationToken;
    
    /**
     * Email del usuario registrado.
     */
    private String email;
    
    /**
     * Tipo de usuario (CLIENTE o PROVEEDOR).
     */
    private String userType;
    
    /**
     * Mensaje informativo para el cliente.
     */
    private String message;
    
    /**
     * ID del usuario registrado.
     */
    private Long userId;
}
