package com.codefactory.reservasmsauthservice.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminResponseDTO extends UserResponseDTO {
    private String nombreCompleto;
    private String codigoEmpleado;
    private String telefono;
    private LocalDateTime fechaAsignacion;
    private Boolean activo;
    private UUID creadoPor;
}
