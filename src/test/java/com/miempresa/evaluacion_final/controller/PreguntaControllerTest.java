package com.miempresa.evaluacion_final.controller;

import com.miempresa.evaluacion_final.exception.PreguntaNoEncontradaException;
import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionMultiple;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionUnica;
import com.miempresa.evaluacion_final.model.PreguntaVerdaderoFalso;
import com.miempresa.evaluacion_final.model.Tematica;
import com.miempresa.evaluacion_final.service.IPreguntaService;
import com.miempresa.evaluacion_final.service.ITematicaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreguntaControllerTest {

    @Mock
    private IPreguntaService preguntaService;

    @Mock
    private ITematicaService tematicaService;

    @Mock
    private Model model;

    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private PreguntaController preguntaController;

    @Captor
    private ArgumentCaptor<Pregunta> preguntaCaptor;

    private Tematica tematica;
    private Pregunta preguntaAbierta;
    private PreguntaVerdaderoFalso preguntaVF;
    private PreguntaSeleccionUnica preguntaUnica;
    private PreguntaSeleccionMultiple preguntaMultiple;
    private Page<Pregunta> paginaPreguntas;

    @BeforeEach
    void setUp() {
        tematica = new Tematica("Java Básico");
        tematica.setId(1L);

        preguntaAbierta = new Pregunta("¿Qué es Java?", "Un lenguaje", tematica);
        preguntaAbierta.setId(1L);

        preguntaVF = new PreguntaVerdaderoFalso("Java es OOP", true, tematica);
        preguntaVF.setId(2L);

        preguntaUnica = new PreguntaSeleccionUnica("¿Qué es int?", "int|long|String", "int", tematica);
        preguntaUnica.setId(3L);

        preguntaMultiple = new PreguntaSeleccionMultiple("¿Tipos primitivos?", "int|String|boolean", "int,boolean", tematica);
        preguntaMultiple.setId(4L);

        paginaPreguntas = new PageImpl<>(List.of(preguntaAbierta, preguntaVF, preguntaUnica, preguntaMultiple));

        bindingResult = new BeanPropertyBindingResult(preguntaAbierta, "pregunta");
    }

    @Test
    void listar_SinFiltros_DeberiaRetornarVistaListar() {
        when(preguntaService.listarFiltradas(eq(null), eq(null), any(Pageable.class))).thenReturn(paginaPreguntas);
        when(tematicaService.listarTodas()).thenReturn(List.of(tematica));

        String vista = preguntaController.listar(model, null, null,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));

        assertEquals("pregunta/listar", vista);
        verify(model).addAttribute("preguntas", paginaPreguntas);
        verify(model).addAttribute("tematicas", List.of(tematica));
        verify(model).addAttribute("tematicaId", null);
        verify(model).addAttribute("tipoFiltro", null);
    }

    @Test
    void listar_ConFiltros_DeberiaRetornarVistaListar() {
        when(preguntaService.listarFiltradas(eq(1L), eq("ABIERTA"), any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(preguntaAbierta)));
        when(tematicaService.listarTodas()).thenReturn(List.of(tematica));

        String vista = preguntaController.listar(model, 1L, "ABIERTA",
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));

        assertEquals("pregunta/listar", vista);
        verify(model).addAttribute(eq("preguntas"), any(Page.class));
        verify(model).addAttribute("tematicaId", 1L);
        verify(model).addAttribute("tipoFiltro", "ABIERTA");
    }

    @Test
    void mostrarFormularioNueva_DeberiaRetornarVistaForm() {
        when(tematicaService.listarTodas()).thenReturn(List.of(tematica));

        String vista = preguntaController.mostrarFormularioNueva(model);

        assertEquals("pregunta/form", vista);
        verify(model).addAttribute(eq("pregunta"), any(Pregunta.class));
        verify(model).addAttribute("tematicas", List.of(tematica));
    }

    @Test
    void mostrarFormularioEditar_Existente_DeberiaRetornarVistaForm() {
        when(preguntaService.obtenerPorId(1L)).thenReturn(Optional.of(preguntaAbierta));
        when(tematicaService.listarTodas()).thenReturn(List.of(tematica));

        String vista = preguntaController.mostrarFormularioEditar(1L, model);

        assertEquals("pregunta/form", vista);
        verify(model).addAttribute("pregunta", preguntaAbierta);
        verify(model).addAttribute("tematicas", List.of(tematica));
    }

    @Test
    void mostrarFormularioEditar_NoExistente_DeberiaLanzarExcepcion() {
        when(preguntaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        assertThrows(PreguntaNoEncontradaException.class,
                () -> preguntaController.mostrarFormularioEditar(99L, model));
    }

    @Test
    void crear_AbiertaValida_DeberiaRedirigir() {
        when(preguntaService.guardar(any(Pregunta.class))).thenReturn(preguntaAbierta);

        String vista = preguntaController.crear("ABIERTA", false, null, null, null,
                preguntaAbierta, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/preguntas", vista);
        verify(preguntaService).guardar(preguntaCaptor.capture());
        assertInstanceOf(Pregunta.class, preguntaCaptor.getValue());
        verify(redirectAttributes).addFlashAttribute("success", "Pregunta creada correctamente");
    }

    @Test
    void crear_VFValida_DeberiaRedirigir() {
        Pregunta base = new Pregunta("Java es OOP", null, tematica);
        when(preguntaService.guardar(any(Pregunta.class))).thenReturn(preguntaVF);

        String vista = preguntaController.crear("V_F", true, null, null, null,
                base, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/preguntas", vista);
        verify(preguntaService).guardar(preguntaCaptor.capture());
        Pregunta guardada = preguntaCaptor.getValue();
        assertInstanceOf(PreguntaVerdaderoFalso.class, guardada);
        assertTrue(((PreguntaVerdaderoFalso) guardada).isEsVerdadero());
    }

    @Test
    void crear_UnicaValida_DeberiaRedirigir() {
        Pregunta base = new Pregunta("¿Qué es int?", null, tematica);
        String opciones = "int|long|String";
        String opcionCorrecta = "int";
        when(preguntaService.guardar(any(Pregunta.class))).thenReturn(preguntaUnica);

        String vista = preguntaController.crear("UNICA", false, opciones, opcionCorrecta, null,
                base, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/preguntas", vista);
        verify(preguntaService).guardar(preguntaCaptor.capture());
        Pregunta guardada = preguntaCaptor.getValue();
        assertInstanceOf(PreguntaSeleccionUnica.class, guardada);
        assertEquals("int", ((PreguntaSeleccionUnica) guardada).getOpcionCorrecta());
    }

    @Test
    void crear_MultipleValida_DeberiaRedirigir() {
        Pregunta base = new Pregunta("¿Tipos primitivos?", null, tematica);
        String opciones = "int|String|boolean";
        String opcionesCorrectas = "int,boolean";
        when(preguntaService.guardar(any(Pregunta.class))).thenReturn(preguntaMultiple);

        String vista = preguntaController.crear("MULTIPLE", false, opciones, null, opcionesCorrectas,
                base, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/preguntas", vista);
        verify(preguntaService).guardar(preguntaCaptor.capture());
        Pregunta guardada = preguntaCaptor.getValue();
        assertInstanceOf(PreguntaSeleccionMultiple.class, guardada);
        assertEquals("int,boolean", ((PreguntaSeleccionMultiple) guardada).getOpcionesCorrectas());
    }

    @Test
    void crear_ConErroresDeValidacion_DeberiaRetornarForm() {
        bindingResult.reject("error");
        when(tematicaService.listarTodas()).thenReturn(List.of(tematica));

        String vista = preguntaController.crear("ABIERTA", false, null, null, null,
                preguntaAbierta, bindingResult, model, redirectAttributes);

        assertEquals("pregunta/form", vista);
        verify(preguntaService, never()).guardar(any());
    }

    @Test
    void crear_AbiertaConRespuestaVacia_DeberiaTenerError() {
        Pregunta preguntaSinRespuesta = new Pregunta("¿Qué es Java?", "", tematica);
        when(tematicaService.listarTodas()).thenReturn(List.of(tematica));

        String vista = preguntaController.crear("ABIERTA", false, null, null, null,
                preguntaSinRespuesta, bindingResult, model, redirectAttributes);

        assertEquals("pregunta/form", vista);
        assertTrue(bindingResult.hasErrors());
        verify(preguntaService, never()).guardar(any());
    }

    @Test
    void crear_AbiertaConRespuestaNull_DeberiaTenerError() {
        Pregunta preguntaSinRespuesta = new Pregunta("¿Qué es Java?", null, tematica);
        when(tematicaService.listarTodas()).thenReturn(List.of(tematica));

        String vista = preguntaController.crear("ABIERTA", false, null, null, null,
                preguntaSinRespuesta, bindingResult, model, redirectAttributes);

        assertEquals("pregunta/form", vista);
        assertTrue(bindingResult.hasErrors());
        verify(preguntaService, never()).guardar(any());
    }

    @Test
    void actualizar_Valido_DeberiaRedirigir() {
        when(preguntaService.guardar(any(Pregunta.class))).thenReturn(preguntaAbierta);

        String vista = preguntaController.actualizar(1L, "ABIERTA", false, null, null, null,
                preguntaAbierta, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/preguntas", vista);
        verify(preguntaService).guardar(preguntaCaptor.capture());
        assertEquals(1L, preguntaCaptor.getValue().getId());
        verify(redirectAttributes).addFlashAttribute("success", "Pregunta actualizada correctamente");
    }

    @Test
    void actualizar_VF_DeberiaRedirigir() {
        Pregunta base = new Pregunta("Java es OOP", null, tematica);
        when(preguntaService.guardar(any(Pregunta.class))).thenReturn(preguntaVF);

        String vista = preguntaController.actualizar(2L, "V_F", false, null, null, null,
                base, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/preguntas", vista);
        verify(preguntaService).guardar(preguntaCaptor.capture());
        assertEquals(2L, preguntaCaptor.getValue().getId());
        assertInstanceOf(PreguntaVerdaderoFalso.class, preguntaCaptor.getValue());
    }

    @Test
    void actualizar_ConErrores_DeberiaRetornarForm() {
        bindingResult.reject("error");
        when(tematicaService.listarTodas()).thenReturn(List.of(tematica));

        String vista = preguntaController.actualizar(1L, "ABIERTA", false, null, null, null,
                preguntaAbierta, bindingResult, model, redirectAttributes);

        assertEquals("pregunta/form", vista);
        verify(preguntaService, never()).guardar(any());
    }

    @Test
    void eliminar_Existente_DeberiaRedirigir() {
        doNothing().when(preguntaService).eliminar(1L);

        String vista = preguntaController.eliminar(1L, redirectAttributes);

        assertEquals("redirect:/preguntas", vista);
        verify(preguntaService).eliminar(1L);
        verify(redirectAttributes).addFlashAttribute("success", "Pregunta eliminada correctamente");
    }

    @Test
    void eliminar_NoExistente_DeberiaRedirigirSinError() {
        doNothing().when(preguntaService).eliminar(99L);

        String vista = preguntaController.eliminar(99L, redirectAttributes);

        assertEquals("redirect:/preguntas", vista);
        verify(preguntaService).eliminar(99L);
    }
}