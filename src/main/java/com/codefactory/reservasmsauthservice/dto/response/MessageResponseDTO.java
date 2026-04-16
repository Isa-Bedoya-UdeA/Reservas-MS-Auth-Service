package com.codefactory.reservasmsauthservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    private String message;
    private boolean success;
    private Instant timestamp;

    public MessageResponseDTO(String message) {
        this.message = message;
        this.success = true;
        this.timestamp = Instant.now();
    }
}