package com.miempresa.evaluacion_final.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.service.IPreguntaService;
import com.miempresa.evaluacion_final.service.ITematicaService;

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
    public String listar(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        model.addAttribute("preguntas", preguntaService.listarTodas(pageable));
        return "pregunta/listar";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("pregunta", new Pregunta());
        model.addAttribute("tematicas", tematicaService.listarTodas());
        return "pregunta/form";
    }

    @PostMapping("/guardar")
    public String crear(@ModelAttribute Pregunta pregunta, RedirectAttributes redirect) {
        preguntaService.guardar(pregunta);
        redirect.addFlashAttribute("success", "Pregunta creada correctamente");
        return "redirect:/preguntas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Pregunta pregunta = preguntaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada: " + id));
        model.addAttribute("pregunta", pregunta);
        model.addAttribute("tematicas", tematicaService.listarTodas());
        return "pregunta/form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Pregunta pregunta, RedirectAttributes redirect) {
        pregunta.setId(id);
        preguntaService.guardar(pregunta);
        redirect.addFlashAttribute("success", "Pregunta actualizada correctamente");
        return "redirect:/preguntas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        preguntaService.eliminar(id);
        redirect.addFlashAttribute("success", "Pregunta eliminada correctamente");
        return "redirect:/preguntas";
    }
}