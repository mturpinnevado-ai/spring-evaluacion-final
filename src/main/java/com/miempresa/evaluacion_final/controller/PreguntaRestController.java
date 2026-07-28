package com.miempresa.evaluacion_final.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.miempresa.evaluacion_final.exception.PreguntaNoEncontradaException;
import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.model.PreguntaDTO;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionMultiple;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionUnica;
import com.miempresa.evaluacion_final.model.PreguntaVerdaderoFalso;
import com.miempresa.evaluacion_final.model.Tematica;
import com.miempresa.evaluacion_final.repository.TematicaRepository;
import com.miempresa.evaluacion_final.service.IPreguntaService;

@RestController
@RequestMapping("/api/preguntas")
public class PreguntaRestController {

    private final IPreguntaService preguntaService;
    private final TematicaRepository tematicaRepository;

    public PreguntaRestController(IPreguntaService preguntaService, TematicaRepository tematicaRepository) {
        this.preguntaService = preguntaService;
        this.tematicaRepository = tematicaRepository;
    }

    @GetMapping
    public Page<PreguntaDTO> listar(
            @RequestParam(required = false) Long tematicaId,
            @RequestParam(required = false) String tipo,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return preguntaService.listarFiltradas(tematicaId, tipo, pageable)
                .map(PreguntaDTO::fromEntity);
    }

    @GetMapping("/{id}")
    public PreguntaDTO obtenerPorId(@PathVariable Long id) {
        return preguntaService.obtenerPorId(id)
                .map(PreguntaDTO::fromEntity)
                .orElseThrow(() -> new PreguntaNoEncontradaException(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PreguntaDTO crear(@RequestBody Map<String, Object> body) {
        String tipo = (String) body.get("tipo");
        String enunciado = (String) body.get("enunciado");
        Long tematicaId = Long.valueOf(body.get("tematicaId").toString());

        Tematica tematica = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new RuntimeException("Temática no encontrada: " + tematicaId));

        Pregunta pregunta = switch (tipo != null ? tipo : "ABIERTA") {
            case "V_F" -> {
                boolean esVerdadero = Boolean.TRUE.equals(body.get("esVerdadero"));
                yield new PreguntaVerdaderoFalso(enunciado, esVerdadero, tematica);
            }
            case "UNICA" -> {
                String opciones = (String) body.get("opciones");
                String opcionCorrecta = (String) body.get("opcionCorrecta");
                yield new PreguntaSeleccionUnica(enunciado, opciones, opcionCorrecta, tematica);
            }
            case "MULTIPLE" -> {
                String opciones = (String) body.get("opciones");
                String opcionesCorrectas = (String) body.get("opcionesCorrectas");
                yield new PreguntaSeleccionMultiple(enunciado, opciones, opcionesCorrectas, tematica);
            }
            default -> {
                String respuesta = (String) body.get("respuesta");
                yield new Pregunta(enunciado, respuesta, tematica);
            }
        };

        return PreguntaDTO.fromEntity(preguntaService.guardar(pregunta));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public PreguntaDTO actualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        preguntaService.obtenerPorId(id)
                .orElseThrow(() -> new PreguntaNoEncontradaException(id));

        String tipo = (String) body.get("tipo");
        String enunciado = (String) body.get("enunciado");
        Long tematicaId = Long.valueOf(body.get("tematicaId").toString());

        Tematica tematica = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new RuntimeException("Temática no encontrada: " + tematicaId));

        Pregunta pregunta = switch (tipo != null ? tipo : "ABIERTA") {
            case "V_F" -> {
                boolean esVerdadero = Boolean.TRUE.equals(body.get("esVerdadero"));
                yield new PreguntaVerdaderoFalso(enunciado, esVerdadero, tematica);
            }
            case "UNICA" -> {
                String opciones = (String) body.get("opciones");
                String opcionCorrecta = (String) body.get("opcionCorrecta");
                yield new PreguntaSeleccionUnica(enunciado, opciones, opcionCorrecta, tematica);
            }
            case "MULTIPLE" -> {
                String opciones = (String) body.get("opciones");
                String opcionesCorrectas = (String) body.get("opcionesCorrectas");
                yield new PreguntaSeleccionMultiple(enunciado, opciones, opcionesCorrectas, tematica);
            }
            default -> {
                String respuesta = (String) body.get("respuesta");
                yield new Pregunta(enunciado, respuesta, tematica);
            }
        };
        pregunta.setId(id);

        return PreguntaDTO.fromEntity(preguntaService.guardar(pregunta));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        preguntaService.obtenerPorId(id)
                .orElseThrow(() -> new PreguntaNoEncontradaException(id));
        preguntaService.eliminar(id);
    }
}