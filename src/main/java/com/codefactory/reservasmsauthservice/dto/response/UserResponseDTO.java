package com.codefactory.reservasmsauthservice.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long idUsuario;
    private String email;
    private String tipoUsuario;
    private Boolean estado;
    private LocalDateTime fechaRegistro;
}
