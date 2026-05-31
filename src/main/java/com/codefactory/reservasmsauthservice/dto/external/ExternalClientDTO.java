package com.codefactory.reservasmsauthservice.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalClientDTO extends RepresentationModel<ExternalClientDTO> {
    private String nombre;
    private String email;
    private String telefono;
    private boolean emailVerificado;
    private boolean activo;
    private String tipoUsuario;
}