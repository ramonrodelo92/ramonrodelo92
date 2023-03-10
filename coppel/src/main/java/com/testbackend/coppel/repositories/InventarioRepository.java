package com.testbackend.coppel.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.testbackend.coppel.models.Inventario;

@Repository
public interface InventarioRepository extends CrudRepository<Inventario, Long> {
    public abstract Inventario findBysku(Long sku);
}
