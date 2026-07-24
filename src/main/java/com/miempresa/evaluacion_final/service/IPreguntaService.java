package com.miempresa.evaluacion_final.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.miempresa.evaluacion_final.model.Pregunta;

public interface IPreguntaService {
	
    Page<Pregunta> listarTodas(Pageable pageable);
    Page<Pregunta> listarFiltradas(Long tematicaId, String tipo, Pageable pageable);
    Optional<Pregunta> obtenerPorId(Long id);
    Pregunta guardar(Pregunta pregunta);
    void eliminar(Long id);

}
