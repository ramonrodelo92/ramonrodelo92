package com.testbackend.coppel.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testbackend.coppel.models.Inventario;
import com.testbackend.coppel.services.InventarioService;
import com.testbackend.coppel.utils.HttpStatusCode;
import com.testbackend.coppel.utils.ResponseMapped;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController()
@RequestMapping("/api/v1")
public class InventarioController {
    @Autowired
    private InventarioService inventarioService;

    private final static Logger LOGGER = LoggerFactory.getLogger(InventarioController.class);

    @GetMapping()
    public ResponseEntity<Object> getInventario() {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            ArrayList<Inventario> inventario = inventarioService.getInventario();

            response.put("Inventario", inventario);
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("Mensaje", e.getMessage());
            LOGGER.error("/inventario - " + e.getMessage());
            return ResponseMapped.setResponse(HttpStatusCode.OK, response, HttpStatus.CONFLICT);
        }
    }
}
