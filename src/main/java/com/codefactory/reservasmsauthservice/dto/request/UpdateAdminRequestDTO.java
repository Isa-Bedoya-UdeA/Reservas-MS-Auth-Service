package com.codefactory.reservasmsauthservice.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAdminRequestDTO {

    @Size(max = 150, message = "El nombre completo no puede exceder 150 caracteres")
    private String nombreCompleto;

    @Pattern(regexp = "^\\d+$", message = "El teléfono solo debe contener números")
    private String telefono;

    @Size(max = 50, message = "El código de empleado no puede exceder 50 caracteres")
    private String codigoEmpleado;

    private Boolean activo;
}
