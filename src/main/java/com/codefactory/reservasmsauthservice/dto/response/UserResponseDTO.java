package com.codefactory.reservasmsauthservice.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponseDTO {
    private UUID idUsuario;
    private String email;
    private String tipoUsuario;
    private String estado;
    private LocalDateTime fechaRegistro;
}
