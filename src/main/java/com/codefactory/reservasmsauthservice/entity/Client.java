package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@Setter
public class Client extends User {

    @Column(nullable = false)
    private String nombre;

    @Column
    private String telefono;
}
