package com.testbackend.coppel.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbackend.coppel.models.Inventario;
import com.testbackend.coppel.repositories.InventarioRepository;

@Service
public class InventarioService {
    @Autowired
    private InventarioRepository inventarioRepository;

    public ArrayList<Inventario> getInventario() {
        return (ArrayList<Inventario>) inventarioRepository.findAll();
    }

    public Inventario saveInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public Inventario getInventarioBySku(Long sku) {
        return inventarioRepository.findBysku(sku);
    }
}
