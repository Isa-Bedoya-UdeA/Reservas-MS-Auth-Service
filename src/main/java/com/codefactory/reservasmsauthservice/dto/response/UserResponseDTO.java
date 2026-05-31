package com.codefactory.reservasmsauthservice.dto.response;

import org.springframework.hateoas.RepresentationModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> {
    private UUID idUsuario;
    private String email;
    private String tipoUsuario;
    private String estado;
    private LocalDateTime fechaRegistro;
}