package com.codefactory.reservasmsauthservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para verificación de email exitosa.
 * Indica que el email ha sido verificado y la cuenta está activa.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationResponseDTO {
    
    private Boolean success;
    private String message;
    private Long userId;
    private String email;
}
