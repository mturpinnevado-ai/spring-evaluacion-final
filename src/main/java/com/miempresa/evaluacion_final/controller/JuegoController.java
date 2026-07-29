package com.miempresa.evaluacion_final.controller;

import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionMultiple;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionUnica;
import com.miempresa.evaluacion_final.model.PreguntaVerdaderoFalso;
import com.miempresa.evaluacion_final.model.SesionJuego;
import com.miempresa.evaluacion_final.model.SesionJuego.RespuestaJuego;
import com.miempresa.evaluacion_final.service.IPreguntaService;
import com.miempresa.evaluacion_final.service.ITematicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/juego")
public class JuegoController {

    private final IPreguntaService preguntaService;
    private final ITematicaService tematicaService;

    public JuegoController(IPreguntaService preguntaService, ITematicaService tematicaService) {
        this.preguntaService = preguntaService;
        this.tematicaService = tematicaService;
    }

    @GetMapping
    public String iniciar(Model model) {
        model.addAttribute("tematicas", tematicaService.listarTodas());
        return "juego/iniciar";
    }

    @PostMapping("/iniciar")
    public String comenzar(@RequestParam(required = false) Long tematicaId, HttpSession session) {
        List<Pregunta> todas = preguntaService.listarParaJuego(tematicaId);
        Collections.shuffle(todas);
        List<Pregunta> seleccionadas = todas.subList(0, Math.min(10, todas.size()));

        SesionJuego sesion = new SesionJuego();
        sesion.setTematicaId(tematicaId);
        sesion.setPreguntas(seleccionadas);
        session.setAttribute("sesionJuego", sesion);

        return "redirect:/juego/pregunta/0";
    }

    @GetMapping("/pregunta/{indice}")
    public String mostrarPregunta(Model model, HttpSession session) {
        SesionJuego sesion = (SesionJuego) session.getAttribute("sesionJuego");
        if (sesion == null || sesion.isTerminado()) {
            return "redirect:/juego";
        }
        Pregunta pregunta = sesion.getPreguntaActual();
        if (pregunta == null) {
            return "redirect:/juego/resultados";
        }
        model.addAttribute("sesion", sesion);
        model.addAttribute("pregunta", pregunta);
        model.addAttribute("indice", sesion.getIndiceActual());
        model.addAttribute("total", sesion.getTotal());
        if ("UNICA".equals(pregunta.getTipo())) {
            model.addAttribute("opcionesList", Arrays.asList(((PreguntaSeleccionUnica) pregunta).getOpciones().split("\\|")));
        } else if ("MULTIPLE".equals(pregunta.getTipo())) {
            model.addAttribute("opcionesList", Arrays.asList(((PreguntaSeleccionMultiple) pregunta).getOpciones().split("\\|")));
        }
        return "juego/pregunta";
    }

    @PostMapping("/responder")
    public String responder(@RequestParam int indice,
                            @RequestParam String tipo,
                            @RequestParam(required = false) String respuesta,
                            @RequestParam(required = false, defaultValue = "false") String esVerdadero,
                            @RequestParam(required = false) String opcionSeleccionada,
                            @RequestParam(value = "opcionesSeleccionadas", required = false) List<String> opcionesSeleccionadas,
                            HttpSession session) {
        SesionJuego sesion = (SesionJuego) session.getAttribute("sesionJuego");
        if (sesion == null || indice != sesion.getIndiceActual()) {
            return "redirect:/juego";
        }
        Pregunta pregunta = sesion.getPreguntaActual();
        if (pregunta == null) {
            return "redirect:/juego/resultados";
        }

        boolean correcta = false;
        String respuestaCorrecta = "";
        String respuestaUsuario = "";

        switch (tipo) {
            case "ABIERTA" -> {
                String correctaStr = pregunta.getRespuesta() != null ? pregunta.getRespuesta().trim() : "";
                String userStr = respuesta != null ? respuesta.trim() : "";
                correcta = correctaStr.equalsIgnoreCase(userStr);
                respuestaCorrecta = correctaStr;
                respuestaUsuario = userStr;
            }
            case "V_F" -> {
                PreguntaVerdaderoFalso vf = (PreguntaVerdaderoFalso) pregunta;
                boolean userBool = "true".equals(esVerdadero);
                correcta = userBool == vf.isEsVerdadero();
                respuestaCorrecta = vf.isEsVerdadero() ? "Verdadero" : "Falso";
                respuestaUsuario = userBool ? "Verdadero" : "Falso";
            }
            case "UNICA" -> {
                PreguntaSeleccionUnica unica = (PreguntaSeleccionUnica) pregunta;
                String userOpt = opcionSeleccionada != null ? opcionSeleccionada.trim() : "";
                correcta = userOpt.equals(unica.getOpcionCorrecta());
                respuestaCorrecta = unica.getOpcionCorrecta();
                respuestaUsuario = userOpt;
            }
            case "MULTIPLE" -> {
                PreguntaSeleccionMultiple multiple = (PreguntaSeleccionMultiple) pregunta;
                if (opcionesSeleccionadas == null) {
                    opcionesSeleccionadas = new ArrayList<>();
                }
                List<String> userOpts = opcionesSeleccionadas.stream()
                        .map(String::trim)
                        .sorted()
                        .collect(Collectors.toList());
                List<String> correctOpts = Arrays.stream(multiple.getOpcionesCorrectas().split(","))
                        .map(String::trim)
                        .sorted()
                        .collect(Collectors.toList());
                correcta = userOpts.equals(correctOpts);
                respuestaCorrecta = multiple.getOpcionesCorrectas();
                respuestaUsuario = String.join(", ", userOpts);
            }
        }

        sesion.getRespuestas().add(new RespuestaJuego(
                pregunta.getId(), pregunta.getEnunciado(), tipo,
                respuestaCorrecta, respuestaUsuario, correcta));
        if (correcta) {
            sesion.setPuntuacion(sesion.getPuntuacion() + 1);
        }
        sesion.setIndiceActual(sesion.getIndiceActual() + 1);

        if (sesion.isTerminado()) {
            return "redirect:/juego/resultados";
        }
        return "redirect:/juego/pregunta/" + sesion.getIndiceActual();
    }

    @GetMapping("/resultados")
    public String resultados(Model model, HttpSession session) {
        SesionJuego sesion = (SesionJuego) session.getAttribute("sesionJuego");
        if (sesion == null) {
            return "redirect:/juego";
        }
        model.addAttribute("sesion", sesion);
        session.removeAttribute("sesionJuego");
        return "juego/resultados";
    }
}