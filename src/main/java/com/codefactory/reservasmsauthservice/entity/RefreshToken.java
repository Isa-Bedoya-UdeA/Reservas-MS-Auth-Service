package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un token de refresco (refresh token).
 * Utilizado para obtener nuevos tokens de acceso sin tener que 
 * realizar un nuevo login.
 */
@Entity
@Table(name = "refresh_token")
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_refresh_token", columnDefinition = "UUID")
    private UUID idRefreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(500)")
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean revoked = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (expiryDate == null) {
            expiryDate = LocalDateTime.now().plusDays(7);
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (revoked == null) {
            revoked = false;
        }
    }
}
