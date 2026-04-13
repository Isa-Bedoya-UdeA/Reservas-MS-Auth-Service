package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que registra intentos de inicio de sesión fallidos.
 * Utilizada para implementar mecanismos de seguridad como bloqueo
 * de cuenta después de múltiples intentos fallidos.
 */
@Entity
@Table(name = "login_attempt")
@Getter
@Setter
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_attempt", columnDefinition = "UUID")
    private UUID idAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(name = "ip_address", columnDefinition = "VARCHAR(45)")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "VARCHAR(500)")
    private String userAgent;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean exitoso = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (exitoso == null) {
            exitoso = false;
        }
    }
}
