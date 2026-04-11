package com.codefactory.reservasmsauthservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta de autenticación.
 * Contiene el token JWT y los datos del usuario autenticado.
 * Utiliza @JsonTypeInfo para preservar información de tipos en la serialización,
 * permitiendo que ClientResponseDTO y ProviderResponseDTO conserven sus campos específicos.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDTO {
    private String token;
    
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
    )
    private UserResponseDTO user;
}
