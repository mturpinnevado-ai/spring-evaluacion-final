package com.miempresa.evaluacion_final.service;

import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionMultiple;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionUnica;
import com.miempresa.evaluacion_final.model.PreguntaVerdaderoFalso;
import com.miempresa.evaluacion_final.repository.PreguntaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Page<Pregunta> listarFiltradas(Long tematicaId, String tipo, Pageable pageable) {
        Class<? extends Pregunta> clase = mapTipoAClase(tipo);
        return preguntaRepository.findFiltered(tematicaId, clase, pageable);
    }

    public List<Pregunta> listarParaJuego(Long tematicaId) {
        if (tematicaId == null) {
            return preguntaRepository.findAll();
        }
        return preguntaRepository.findByTematicaId(tematicaId);
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

    private Class<? extends Pregunta> mapTipoAClase(String tipo) {
        if (tipo == null || tipo.isEmpty()) return null;
        return switch (tipo) {
            case "V_F" -> PreguntaVerdaderoFalso.class;
            case "UNICA" -> PreguntaSeleccionUnica.class;
            case "MULTIPLE" -> PreguntaSeleccionMultiple.class;
            default -> Pregunta.class;
        };
    }
}