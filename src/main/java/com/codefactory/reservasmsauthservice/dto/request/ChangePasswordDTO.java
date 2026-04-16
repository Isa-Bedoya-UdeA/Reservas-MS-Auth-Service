package com.codefactory.reservasmsauthservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para cambiar la contraseña de un usuario autenticado.
 * Contiene la contraseña actual y la nueva contraseña.
 */
@Data
public class ChangePasswordDTO {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String currentPassword;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String newPassword;
}
