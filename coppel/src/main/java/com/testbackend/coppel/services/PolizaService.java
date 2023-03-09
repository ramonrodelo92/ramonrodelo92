package com.testbackend.coppel.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbackend.coppel.models.Poliza;
import com.testbackend.coppel.repositories.PolizaRepository;

@Service
public class PolizaService {

    @Autowired
    PolizaRepository polizaRepository;

    public ArrayList<Poliza> getPolizas() {
        return (ArrayList<Poliza>) polizaRepository.findAll();
    }

    public Poliza getPolizaById(Long polizaId) {
        return this.polizaRepository.findPolizaByIdPoliza(polizaId);
    }

    public Poliza createPoliza(Poliza poliza) {
        return polizaRepository.save(poliza);
    }

}
