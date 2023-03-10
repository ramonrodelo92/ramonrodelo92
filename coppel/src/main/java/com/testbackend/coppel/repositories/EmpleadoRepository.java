package com.testbackend.coppel.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.testbackend.coppel.models.Empleado;

@Repository
public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {

    public abstract Empleado findEmpleadoByIdEmpleado(long id);
}
