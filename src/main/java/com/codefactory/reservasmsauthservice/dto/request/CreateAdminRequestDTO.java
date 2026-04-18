package com.codefactory.reservasmsauthservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAdminRequestDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "El formato del email es inválido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", 
             message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número")
    private String password;

    @NotBlank(message = "El nombre completo no puede estar vacío")
    @Size(max = 150, message = "El nombre completo no puede exceder 150 caracteres")
    private String nombreCompleto;

    @Pattern(regexp = "^\\d+$", message = "El teléfono solo debe contener números")
    private String telefono;

    @Size(max = 50, message = "El código de empleado no puede exceder 50 caracteres")
    private String codigoEmpleado;
}
