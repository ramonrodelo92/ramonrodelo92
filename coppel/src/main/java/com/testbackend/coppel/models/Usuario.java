package com.testbackend.coppel.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(unique = true, nullable = false)
    private String email;
    private String contrasena;
}
