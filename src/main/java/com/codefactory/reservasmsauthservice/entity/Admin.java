package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@Setter
public class Admin extends User {

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "codigo_empleado")
    private String codigoEmpleado;

    @Column
    private String telefono;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_por")
    private UUID creadoPor;

    @PrePersist
    protected void onCreate() {
        if (fechaAsignacion == null) {
            fechaAsignacion = LocalDateTime.now();
        }
    }
}
