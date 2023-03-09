package com.testbackend.coppel.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testbackend.coppel.models.Poliza;
import com.testbackend.coppel.services.PolizaService;
import com.testbackend.coppel.utils.HttpStatusCode;
import com.testbackend.coppel.utils.ResponseMapped;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController()
@RequestMapping("/api/v1")
public class PolizaController {

    @Autowired
    private PolizaService polizaService;

    @GetMapping("/poliza")
    public Map<String, Map<String, Object>> index() {
        Map<String, Map<String, Object>> response = new HashMap<>();
        List<Poliza> polizas = this.polizaService.getPolizas();

        response.put("Meta", Map.of("Status", HttpStatusCode.OK));
        response.put("Data", Map.of("Polizas", polizas));

        return response;
    }

    @GetMapping("/poliza/{id}")
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
    public ResponseEntity<Object> createPoliza(@RequestBody Poliza poliza) {

        Map<String, Object> response = new HashMap<>();

        try {
            this.polizaService.createPoliza(poliza);
            response.put("Data", Map.of("Mensaje", "Poliza creada correctamente"));
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("Mensaje", "Error al crear poliza");
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/poliza/{id}")
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
