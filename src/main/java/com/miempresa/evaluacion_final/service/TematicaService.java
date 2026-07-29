package com.miempresa.evaluacion_final.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.miempresa.evaluacion_final.model.Tematica;
import com.miempresa.evaluacion_final.repository.TematicaRepository;

@Service
public class TematicaService implements ITematicaService{

    private final TematicaRepository tematicaRepository;

    public TematicaService(TematicaRepository tematicaRepository) {
        this.tematicaRepository = tematicaRepository;
    }

    public List<Tematica> listarTodas() {
        return tematicaRepository.findAllByOrderByNombreAsc();
    }
}