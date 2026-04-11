package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "proveedor")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@Setter
public class Provider extends User {

    @Column(name = "nombre_comercial", nullable = false)
    private String nombreComercial;

    // TODO: Create CATEGORIA_SERVICIO entity and add mapping
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(nullable = false)
    private String direccion;

    @Column(name = "telefono_contacto", nullable = false)
    private String telefonoContacto;
}
