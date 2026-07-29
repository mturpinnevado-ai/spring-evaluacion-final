package com.miempresa.evaluacion_final.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.miempresa.evaluacion_final.exception.PreguntaNoEncontradaException;
import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionMultiple;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionUnica;
import com.miempresa.evaluacion_final.model.PreguntaVerdaderoFalso;
import com.miempresa.evaluacion_final.model.Tematica;
import com.miempresa.evaluacion_final.service.IPreguntaService;
import com.miempresa.evaluacion_final.service.ITematicaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/preguntas")
public class PreguntaController {

    private final IPreguntaService preguntaService;
    private final ITematicaService tematicaService;

    public PreguntaController(IPreguntaService preguntaService, ITematicaService tematicaService) {
        this.preguntaService = preguntaService;
        this.tematicaService = tematicaService;
    }

    @GetMapping
    public String listar(Model model,
                         @RequestParam(required = false) Long tematicaId,
                         @RequestParam(required = false) String tipo,
                         @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        model.addAttribute("preguntas", preguntaService.listarFiltradas(tematicaId, tipo, pageable));
        model.addAttribute("tematicas", tematicaService.listarTodas());
        model.addAttribute("tematicaId", tematicaId);
        model.addAttribute("tipoFiltro", tipo);
        return "pregunta/listar";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("pregunta", new Pregunta());
        model.addAttribute("tematicas", tematicaService.listarTodas());
        return "pregunta/form";
    }

    @PostMapping("/guardar")
    public String crear(@RequestParam String tipo,
                        @RequestParam(defaultValue = "false") boolean esVerdadero,
                        @RequestParam(required = false) String opciones,
                        @RequestParam(required = false) String opcionCorrecta,
                        @RequestParam(required = false) String opcionesCorrectas,
                        @Valid @ModelAttribute Pregunta pregunta,
                        BindingResult result, Model model, RedirectAttributes redirect) {
        validarRespuestaAbierta(tipo, pregunta, result);
        if (result.hasErrors()) {
            model.addAttribute("tematicas", tematicaService.listarTodas());
            return "pregunta/form";
        }
        Pregunta entity = crearSegunTipo(tipo, pregunta, esVerdadero, opciones, opcionCorrecta, opcionesCorrectas);
        preguntaService.guardar(entity);
        redirect.addFlashAttribute("success", "Pregunta creada correctamente");
        return "redirect:/preguntas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Pregunta pregunta = preguntaService.obtenerPorId(id)
                .orElseThrow(() -> new PreguntaNoEncontradaException(id));
        model.addAttribute("pregunta", pregunta);
        model.addAttribute("tematicas", tematicaService.listarTodas());
        return "pregunta/form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String tipo,
                             @RequestParam(defaultValue = "false") boolean esVerdadero,
                             @RequestParam(required = false) String opciones,
                             @RequestParam(required = false) String opcionCorrecta,
                             @RequestParam(required = false) String opcionesCorrectas,
                             @Valid @ModelAttribute Pregunta pregunta,
                             BindingResult result, Model model, RedirectAttributes redirect) {
        validarRespuestaAbierta(tipo, pregunta, result);
        if (result.hasErrors()) {
            model.addAttribute("tematicas", tematicaService.listarTodas());
            return "pregunta/form";
        }
        Pregunta entity = crearSegunTipo(tipo, pregunta, esVerdadero, opciones, opcionCorrecta, opcionesCorrectas);
        entity.setId(id);
        preguntaService.guardar(entity);
        redirect.addFlashAttribute("success", "Pregunta actualizada correctamente");
        return "redirect:/preguntas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        preguntaService.eliminar(id);
        redirect.addFlashAttribute("success", "Pregunta eliminada correctamente");
        return "redirect:/preguntas";
    }

    private Pregunta crearSegunTipo(String tipo, Pregunta base, boolean esVerdadero,
                                    String opciones, String opcionCorrecta, String opcionesCorrectas) {
        Tematica tematica = base.getTematica();
        return switch (tipo) {
            case "V_F" -> new PreguntaVerdaderoFalso(base.getEnunciado(), esVerdadero, tematica);
            case "UNICA" -> new PreguntaSeleccionUnica(base.getEnunciado(), opciones, opcionCorrecta, tematica);
            case "MULTIPLE" -> new PreguntaSeleccionMultiple(base.getEnunciado(), opciones, opcionesCorrectas, tematica);
            default -> new Pregunta(base.getEnunciado(), base.getRespuesta(), tematica);
        };
    }

    private void validarRespuestaAbierta(String tipo, Pregunta pregunta, BindingResult result) {
        if ("ABIERTA".equals(tipo)) {
            if (pregunta.getRespuesta() == null || pregunta.getRespuesta().isBlank()) {
                result.rejectValue("respuesta", "pregunta.respuesta.notblank", "La respuesta es obligatoria");
            } else if (pregunta.getRespuesta().length() < 2 || pregunta.getRespuesta().length() > 150) {
                result.rejectValue("respuesta", "pregunta.respuesta.size", "La respuesta debe tener entre 2 y 150 caracteres");
            }
        }
    }
}
