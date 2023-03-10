package com.testbackend.coppel.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testbackend.coppel.models.Empleado;
import com.testbackend.coppel.models.Inventario;
import com.testbackend.coppel.models.Poliza;
import com.testbackend.coppel.services.EmpleadoService;
import com.testbackend.coppel.services.InventarioService;
import com.testbackend.coppel.services.PolizaService;
import com.testbackend.coppel.utils.HttpStatusCode;
import com.testbackend.coppel.utils.ResponseMapped;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController()
@RequestMapping("/api/v1")
public class PolizaController {

    @Autowired
    private PolizaService polizaService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/poliza")
    @Transactional(readOnly = true)
    public Map<String, Map<String, Object>> index() {
        Map<String, Map<String, Object>> response = new HashMap<>();
        List<Poliza> polizas = this.polizaService.getPolizas();

        response.put("Meta", Map.of("Status", HttpStatusCode.OK));
        response.put("Data", Map.of("Polizas", polizas));

        return response;
    }

    @GetMapping("/poliza/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getPolizaById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Poliza foundPoliza = this.polizaService.getPolizaById(id);

        try {
            if (foundPoliza == null) {
                response.put("Mensaje", "Poliza no encontrada");
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.NOT_FOUND);
            }

            response.put("Data", Map.of("Poliza", foundPoliza));
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("Mensaje", "Error al obtener poliza");
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/poliza")
    @Transactional
    public ResponseEntity<Object> createPoliza(@RequestBody Poliza poliza) {

        Map<String, Object> response = new HashMap<>();

        try {
            this.polizaService.createPoliza(poliza);
            response.put("Data", Map.of("Mensaje", "Poliza creada correctamente"));
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("Mensaje", "Error al crear poliza");
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/poliza/{id}")
    @Transactional
    public ResponseEntity<Object> updatePoliza(@PathVariable Long id, @RequestBody Poliza poliza) {
        Map<String, Object> response = new HashMap<>();

        try {
            Poliza foundPoliza = this.polizaService.getPolizaById(id);
            if (foundPoliza == null) {
                response.put("Mensaje", "Poliza no encontrada");
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.NOT_FOUND);
            }

            if (poliza.getIdPoliza() != null) {
                Empleado empleado = empleadoService.getEmpleadoById(poliza.getEmpleado().getIdEmpleado());
                if (empleado == null) {
                    response.put("Mensaje", "No se encontro empleado con id " + poliza.getEmpleado().getIdEmpleado());
                    return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.NOT_FOUND);
                }

                foundPoliza.setEmpleado(empleado);
            }

            if (poliza.getCantidad() != null) {
                var diferenciaCantidad = foundPoliza.getCantidad() - poliza.getCantidad();

                var sumaCantidad = foundPoliza.getArticulo().getCantidad() + foundPoliza.getCantidad();
                if (sumaCantidad < poliza.getCantidad()) {
                    response.put("Mensaje", "Cantidad sobrepasa el inventario disponible.");
                    return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.CONFLICT);
                }

                foundPoliza.setCantidad(poliza.getCantidad());

                Inventario inventario = inventarioService.getInventarioBySku(foundPoliza.getArticulo().getSku());
                inventario.setCantidad(inventario.getCantidad() + diferenciaCantidad);
                inventarioService.saveInventario(inventario);
            }

            foundPoliza.setCantidad(poliza.getCantidad());
            foundPoliza.setFecha(poliza.getFecha());

            polizaService.createPoliza(foundPoliza);
            response.put("Mensaje", "Se actualizo correctamente la poliza " + poliza.getIdPoliza());
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("Mensaje", "Error al actualizar poliza " + poliza.getIdPoliza());
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/poliza/{id}")
    @Transactional
    public ResponseEntity<Object> deletePoliza(@PathVariable Long id) {
        boolean succesDelete = this.polizaService.deletePoliza(id);
        Map<String, Object> response = new HashMap<>();

        if (!succesDelete) {
            response.put("Mensaje", "Error al eliminar poliza");
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("Mensaje", "Poliza eliminada correctamente");
        return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.NO_CONTENT);
    }
}
