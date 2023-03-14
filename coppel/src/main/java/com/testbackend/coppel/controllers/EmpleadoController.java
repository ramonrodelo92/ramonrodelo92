package com.testbackend.coppel.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testbackend.coppel.models.Empleado;
import com.testbackend.coppel.services.EmpleadoService;
import com.testbackend.coppel.utils.HttpStatusCode;
import com.testbackend.coppel.utils.ResponseMapped;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController()
@RequestMapping("/api/v1")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    private final static Logger LOGGER = LoggerFactory.getLogger(EmpleadoController.class);

    @GetMapping("/empleado")
    public ResponseEntity<Object> index() {
        List<Empleado> empleados = empleadoService.getEmpleados();

        Map<String, Object> response = new HashMap<>();

        response.put("Empleados", empleados);

        return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.OK);
    }

    @GetMapping("/empleado/{id}")
    public ResponseEntity<Object> getEmpleadoById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Empleado empleado = empleadoService.getEmpleadoById(id);
            if (empleado == null) {
                response.put("Mensaje", "No existe el empleado con el id " + id);
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.NOT_FOUND);
            }
            response.put("Empleado", empleado);
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("Mensaje", "Error al obtener empleado con el id " + id);
            LOGGER.error("/empleado/" + id, e.getMessage());
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
