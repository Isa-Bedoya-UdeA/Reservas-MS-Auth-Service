package com.codefactory.reservasmsauthservice.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProviderResponseDTO extends UserResponseDTO {
    private String nombreComercial;
    private UUID idCategoria;
    private String direccion;
    private String telefonoContacto;
}
