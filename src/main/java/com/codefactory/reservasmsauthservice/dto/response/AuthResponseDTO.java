package com.codefactory.reservasmsauthservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta de autenticación.
 * Contiene el token JWT y los datos del usuario autenticado.
 * Utiliza @JsonTypeInfo con Id.NAME y @JsonSubTypes para permitir polimorfismo seguro,
 * restringiendo las clases que pueden ser deserializadas a ClientResponseDTO y ProviderResponseDTO.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDTO {
    private String token;
    
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "userType"
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = ClientResponseDTO.class, name = "client"),
        @JsonSubTypes.Type(value = ProviderResponseDTO.class, name = "provider"),
        @JsonSubTypes.Type(value = AdminResponseDTO.class, name = "admin")
    })
    private UserResponseDTO user;
}
