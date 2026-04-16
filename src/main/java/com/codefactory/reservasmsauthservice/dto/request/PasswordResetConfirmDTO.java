package com.codefactory.reservasmsauthservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para confirmar el restablecimiento de contraseña.
 * Contiene el token y la nueva contraseña.
 */
@Data
public class PasswordResetConfirmDTO {

    @NotBlank(message = "El token es obligatorio")
    private String token;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String newPassword;
}
