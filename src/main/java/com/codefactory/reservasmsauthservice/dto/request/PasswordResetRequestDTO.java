package com.codefactory.reservasmsauthservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para solicitar el restablecimiento de contraseña.
 * Contiene el email del usuario que olvidó su contraseña.
 */
@Data
public class PasswordResetRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;
}
