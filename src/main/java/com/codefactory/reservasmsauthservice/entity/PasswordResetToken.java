package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un token de restablecimiento de contraseña.
 * Se genera cuando un usuario solicita cambiar su contraseña
 * y debe ser validado antes de poder establecer una nueva contraseña.
 */
@Entity
@Table(name = "token_reset_password")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_token", columnDefinition = "UUID")
    private UUID idResetToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String token;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean usado = false;

    @Column(name = "fecha_uso")
    private LocalDateTime fechaUso;

    @Column(name = "direccion_ip_solicitud", nullable = false, columnDefinition = "VARCHAR(45)")
    private String ipAddress;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (expiryDate == null) {
            expiryDate = LocalDateTime.now().plusHours(24);
        }
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (usado == null) {
            usado = false;
        }
    }
}
