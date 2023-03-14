package com.testbackend.coppel.utils;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
public class PolizaUpdateValidator {
    @Range(min = 1, message = "El campo cantidad debe ser mayor a 0.")
    private Integer cantidad;

    @Range(min = 1, message = "El campo id_empleado debe ser mayor a 0.")
    private Long idEmpleado;
}
