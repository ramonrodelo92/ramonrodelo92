package com.testbackend.coppel.utils;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
public class PolizaCreateValidator {
    @NotNull(message = "El campo cantidad es requerido.")
    @Range(min = 1, message = "El campo cantidad debe ser mayor a 0.")
    private Integer cantidad;

    @NotNull(message = "El campo id_empleado es requerido.")
    @Range(min = 1, message = "El campo idEmpleado debe ser mayor a 0.")
    private Long idEmpleado;

    @NotNull(message = "El campo sku es requerido.")
    private Long sku;
}
