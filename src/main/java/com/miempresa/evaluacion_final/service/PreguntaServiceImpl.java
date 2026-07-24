package com.miempresa.evaluacion_final.service;

import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.repository.PreguntaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreguntaServiceImpl implements IPreguntaService{

    private final PreguntaRepository preguntaRepository;

    public PreguntaServiceImpl(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    public Page<Pregunta> listarTodas(Pageable pageable) {
        return preguntaRepository.findAllByOrderByTematicaIdAscIdAsc(pageable);
    }

    public Optional<Pregunta> obtenerPorId(Long id) {
        return preguntaRepository.findById(id);
    }

    public Pregunta guardar(Pregunta pregunta) {
        return preguntaRepository.save(pregunta);
    }

    public void eliminar(Long id) {
        preguntaRepository.deleteById(id);
    }
}