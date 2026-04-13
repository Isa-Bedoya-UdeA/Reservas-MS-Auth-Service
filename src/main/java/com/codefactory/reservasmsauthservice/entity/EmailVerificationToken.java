package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un token de verificación de email.
 * Se genera cuando un usuario se registra y debe ser validado
 * antes de poder iniciar sesión.
 * 
 * Mapea exactamente a la tabla email_verification_token del DDL Supabase.
 */
@Entity
@Table(name = "email_verification_token")
@Getter
@Setter
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_token", columnDefinition = "UUID")
    private UUID idToken;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private User user;
    
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(500)")
    private String token;
    
    @Column(name = "fecha_expiracion", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime fechaExpiracion;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean usado = false;
    
    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (fechaExpiracion == null) {
            fechaExpiracion = LocalDateTime.now().plusHours(24);
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (usado == null) {
            usado = false;
        }
    }
}
