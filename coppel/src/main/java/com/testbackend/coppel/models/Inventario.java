package com.testbackend.coppel.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @Column(unique = true, nullable = false)
    private Long sku;

    private String nombre;
    private Integer cantidad;

}
