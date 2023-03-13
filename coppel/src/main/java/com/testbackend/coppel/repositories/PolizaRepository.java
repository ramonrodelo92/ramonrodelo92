package com.testbackend.coppel.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.testbackend.coppel.models.Poliza;

@Repository
public interface PolizaRepository extends CrudRepository<Poliza, Long> {

    public abstract Poliza findPolizaByIdPoliza(Long idPoliza);

    public abstract void deleteById(Long idPoliza);

}
