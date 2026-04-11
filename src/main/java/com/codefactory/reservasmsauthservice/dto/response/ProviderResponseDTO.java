package com.codefactory.reservasmsauthservice.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProviderResponseDTO extends UserResponseDTO {
    private String nombreComercial;
    private Long idCategoria;
    private String direccion;
    private String telefonoContacto;
}
