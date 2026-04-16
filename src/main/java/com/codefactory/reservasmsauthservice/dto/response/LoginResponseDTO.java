package com.codefactory.reservasmsauthservice.dto.response;

import com.codefactory.reservasmsauthservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String role;
    private UUID userId;
    private String email;

    public static LoginResponseDTO from(String accessToken, String refreshToken, User user) {
        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getTipoUsuario().name())
                .userId(user.getIdUsuario())
                .email(user.getEmail())
                .build();
    }
}
