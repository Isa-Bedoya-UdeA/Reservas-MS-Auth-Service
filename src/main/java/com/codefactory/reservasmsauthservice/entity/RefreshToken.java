package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un token de refresco (refresh token).
 * Utilizado para obtener nuevos tokens de acceso sin tener que 
 * realizar un nuevo login.
 */
@Entity
@Table(name = "token_refresh")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_token", columnDefinition = "UUID")
    private UUID idRefreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(500)")
    private String token;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "revocado", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean revocado = false;

    @Column(name = "fecha_revocacion")
    private LocalDateTime fechaRevocacion;

    @Column(name = "info_dispositivo", columnDefinition = "VARCHAR(255)")
    private String infoDispositivo;

    @Column(name = "direccion_ip", nullable = false, columnDefinition = "VARCHAR(45)")
    private String direccionIp;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (expiryDate == null) {
            expiryDate = LocalDateTime.now().plusDays(7);
        }
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (revocado == null) {
            revocado = false;
        }
    }
}
