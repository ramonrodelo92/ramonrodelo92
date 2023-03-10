package com.testbackend.coppel.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbackend.coppel.models.Empleado;
import com.testbackend.coppel.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public ArrayList<Empleado> getEmpleados() {
        return (ArrayList<Empleado>) empleadoRepository.findAll();
    }

    public Empleado getEmpleadoById(Long empleadoId) {
        return this.empleadoRepository.findEmpleadoByIdEmpleado(empleadoId);
    }

    public Empleado createPoliza(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public boolean deletePoliza(Long empleadoId) {

        try {
            empleadoRepository.deleteById(empleadoId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
