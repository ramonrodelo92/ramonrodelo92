package com.testbackend.coppel.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "polizas")
public class Poliza implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idPoliza;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idEmpleado")
    private Empleado empleado;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "sku")
    private Inventario articulo;

    @JsonIgnoreProperties
    private Integer cantidad;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fecha;

    @PrePersist
    public void prePersist() {
        fecha = new Date();
    }

}
