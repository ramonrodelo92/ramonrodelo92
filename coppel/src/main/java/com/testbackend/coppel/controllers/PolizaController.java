package com.testbackend.coppel.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.testbackend.coppel.utils.PolizaCreateValidator;
import com.testbackend.coppel.utils.PolizaUpdateValidator;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(PolizaController.class);

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

            response.put("Poliza", foundPoliza);
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("Mensaje", "Error al obtener poliza");
            LOGGER.error("/poliza/" + id, e.getMessage());
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/poliza")
    @Transactional
    public ResponseEntity<Object> createPoliza(@Valid @RequestBody PolizaCreateValidator poliza) {

        Map<String, Object> response = new HashMap<String, Object>();
        try {
            Integer cantidad = poliza.getCantidad();
            Empleado empleado = empleadoService.getEmpleadoById(poliza.getIdEmpleado().longValue());
            if (empleado == null) {
                response.put("Mensaje", "Id empleado no existe.");
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.CONFLICT);
            }
            Inventario sku = inventarioService.getInventarioBySku(poliza.getSku().longValue());
            if (sku == null) {
                response.put("Mensaje", "SKU no existe.");
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.CONFLICT);
            }
            if (sku.getCantidad() < cantidad) {
                response.put("Mensaje", "Cantidad mayor a disponible en inventario.");
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.CONFLICT);
            }

            sku.setCantidad(sku.getCantidad() - cantidad);
            inventarioService.saveInventario(sku);

            Poliza nuevaPoliza = new Poliza();
            nuevaPoliza.setCantidad(cantidad);
            nuevaPoliza.setEmpleado(empleado);
            nuevaPoliza.setArticulo(sku);

            polizaService.createPoliza(nuevaPoliza);
            return ResponseMapped.setResponse(HttpStatusCode.OK, nuevaPoliza, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("Mensaje", "Ha ocurrido un error en los grabados de póliza.");
            response.put("Error", e.getMessage());
            LOGGER.error("/poliza - " + e.getMessage());
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/poliza/{id}")
    @Transactional
    public ResponseEntity<Object> updatePoliza(@PathVariable Long id,
            @Valid @RequestBody PolizaUpdateValidator poliza) {
        Map<String, Object> response = new HashMap<>();

        try {
            Poliza foundPoliza = this.polizaService.getPolizaById(id);
            if (foundPoliza == null) {
                response.put("Mensaje", "Poliza no encontrada");
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.NOT_FOUND);
            }

            Long idEmpleado = poliza.getIdEmpleado().longValue();
            if (poliza.getIdEmpleado() != null) {
                Empleado empleado = empleadoService.getEmpleadoById(idEmpleado);
                if (empleado == null) {
                    response.put("Mensaje",
                            "No se encontro empleado con id " + idEmpleado);
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
            foundPoliza.setFecha(foundPoliza.getFecha());

            polizaService.createPoliza(foundPoliza);
            response.put("Mensaje", "Se actualizó correctamente la póliza: " + foundPoliza.getIdPoliza());
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("Mensaje", "Error al actualizar poliza");
            LOGGER.error("/poliza - " + e.getMessage());
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/poliza/{id}")
    @Transactional
    public ResponseEntity<Object> deletePoliza(@PathVariable Long id) {
        Poliza foundPoliza = polizaService.getPolizaById(id);
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            if (foundPoliza == null) {
                response.put("Mensaje", "Póliza no encontrada");
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.NOT_FOUND);
            }

            Long editedSku = foundPoliza.getArticulo().getSku().longValue();
            boolean deleted = polizaService.deletePoliza(id);
            if (deleted) {
                Inventario sku = inventarioService.getInventarioBySku(editedSku);
                sku.setCantidad(sku.getCantidad() + foundPoliza.getCantidad());
                inventarioService.saveInventario(sku);
                response.put("Mensaje", "Se eliminó correctamente la póliza: " + id);
                return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.OK);

            } else {
                response.put("Mensaje", "Error al intentar eliminar la póliza: " + id);
                return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.OK);
            }

        } catch (Exception e) {
            response.put("Mensaje", "Error al eliminar la póliza: " + id);
            response.put("Error", e.getMessage());
            LOGGER.error("/poliza - " + e.getMessage());
            return ResponseMapped.setResponse(HttpStatusCode.FAILURE, response, HttpStatus.CONFLICT);
        }
    }
}
