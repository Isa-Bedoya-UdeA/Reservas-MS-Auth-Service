package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un token de restablecimiento de contraseña.
 * Se genera cuando un usuario solicita cambiar su contraseña
 * y debe ser validado antes de poder establecer una nueva contraseña.
 */
@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reset_token", columnDefinition = "UUID")
    private UUID idResetToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(500)")
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean usado = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (expiryDate == null) {
            expiryDate = LocalDateTime.now().plusHours(24);
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (usado == null) {
            usado = false;
        }
    }
}
