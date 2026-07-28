package com.miempresa.evaluacion_final.service;

import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionMultiple;
import com.miempresa.evaluacion_final.model.PreguntaSeleccionUnica;
import com.miempresa.evaluacion_final.model.PreguntaVerdaderoFalso;
import com.miempresa.evaluacion_final.model.Tematica;
import com.miempresa.evaluacion_final.repository.PreguntaRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreguntaServiceImplTest {

    @Mock
    private PreguntaRepository preguntaRepository;

    @InjectMocks
    private PreguntaServiceImpl preguntaService;

    @Captor
    private ArgumentCaptor<Pregunta> preguntaCaptor;

    private Tematica tematica;
    private Pregunta preguntaAbierta;
    private PreguntaVerdaderoFalso preguntaVF;
    private PreguntaSeleccionUnica preguntaUnica;
    private PreguntaSeleccionMultiple preguntaMultiple;
    private Pageable pageable;

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

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void listarTodas_DeberiaRetornarPaginaDePreguntas() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaAbierta, preguntaVF));
        when(preguntaRepository.findAllByOrderByTematicaIdAscIdAsc(pageable)).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarTodas(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        verify(preguntaRepository).findAllByOrderByTematicaIdAscIdAsc(pageable);
    }

    @Test
    void listarFiltradas_SinFiltros_DeberiaRetornarTodas() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaAbierta, preguntaVF, preguntaUnica, preguntaMultiple));
        when(preguntaRepository.findFiltered(eq(null), eq(null), eq(pageable))).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarFiltradas(null, null, pageable);

        assertNotNull(resultado);
        assertEquals(4, resultado.getContent().size());
        verify(preguntaRepository).findFiltered(null, null, pageable);
    }

    @Test
    void listarFiltradas_PorTematica_DeberiaFiltrar() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaAbierta, preguntaVF));
        when(preguntaRepository.findFiltered(eq(1L), eq(null), eq(pageable))).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarFiltradas(1L, null, pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        verify(preguntaRepository).findFiltered(1L, null, pageable);
    }

    @Test
    void listarFiltradas_PorTipoAbierta_DeberiaFiltrar() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaAbierta));
        when(preguntaRepository.findFiltered(eq(null), eq(Pregunta.class), eq(pageable))).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarFiltradas(null, "ABIERTA", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(preguntaRepository).findFiltered(null, Pregunta.class, pageable);
    }

    @Test
    void listarFiltradas_PorTipoVF_DeberiaFiltrar() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaVF));
        when(preguntaRepository.findFiltered(eq(null), eq(PreguntaVerdaderoFalso.class), eq(pageable))).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarFiltradas(null, "V_F", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(preguntaRepository).findFiltered(null, PreguntaVerdaderoFalso.class, pageable);
    }

    @Test
    void listarFiltradas_PorTipoUnica_DeberiaFiltrar() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaUnica));
        when(preguntaRepository.findFiltered(eq(null), eq(PreguntaSeleccionUnica.class), eq(pageable))).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarFiltradas(null, "UNICA", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(preguntaRepository).findFiltered(null, PreguntaSeleccionUnica.class, pageable);
    }

    @Test
    void listarFiltradas_PorTipoMultiple_DeberiaFiltrar() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaMultiple));
        when(preguntaRepository.findFiltered(eq(null), eq(PreguntaSeleccionMultiple.class), eq(pageable))).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarFiltradas(null, "MULTIPLE", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(preguntaRepository).findFiltered(null, PreguntaSeleccionMultiple.class, pageable);
    }

    @Test
    void listarFiltradas_ConTipoYTematica_DeberiaFiltrarPorAmbos() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaAbierta));
        when(preguntaRepository.findFiltered(eq(1L), eq(Pregunta.class), eq(pageable))).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarFiltradas(1L, "ABIERTA", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(preguntaRepository).findFiltered(1L, Pregunta.class, pageable);
    }

    @Test
    void listarFiltradas_TipoVacio_DeberiaPasarNullComoClase() {
        Page<Pregunta> paginaEsperada = new PageImpl<>(List.of(preguntaAbierta));
        when(preguntaRepository.findFiltered(eq(null), eq(null), eq(pageable))).thenReturn(paginaEsperada);

        Page<Pregunta> resultado = preguntaService.listarFiltradas(null, "", pageable);

        assertNotNull(resultado);
        verify(preguntaRepository).findFiltered(null, null, pageable);
    }

    @Test
    void obtenerPorId_Existente_DeberiaRetornarOptionalConPregunta() {
        when(preguntaRepository.findById(1L)).thenReturn(Optional.of(preguntaAbierta));

        Optional<Pregunta> resultado = preguntaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("¿Qué es Java?", resultado.get().getEnunciado());
        verify(preguntaRepository).findById(1L);
    }

    @Test
    void obtenerPorId_NoExistente_DeberiaRetornarOptionalVacio() {
        when(preguntaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pregunta> resultado = preguntaService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
        verify(preguntaRepository).findById(99L);
    }

    @Test
    void guardar_DeberiaGuardarYRetornarPregunta() {
        when(preguntaRepository.save(preguntaAbierta)).thenReturn(preguntaAbierta);

        Pregunta resultado = preguntaService.guardar(preguntaAbierta);

        assertNotNull(resultado);
        assertEquals("¿Qué es Java?", resultado.getEnunciado());
        verify(preguntaRepository).save(preguntaAbierta);
    }

    @Test
    void guardar_NuevaPregunta_DeberiaDelegarEnSave() {
        Pregunta nueva = new Pregunta("Nueva pregunta", "Respuesta", tematica);
        when(preguntaRepository.save(nueva)).thenReturn(nueva);

        Pregunta resultado = preguntaService.guardar(nueva);

        assertNotNull(resultado);
        assertNull(resultado.getId());
        verify(preguntaRepository).save(preguntaCaptor.capture());
        assertEquals("Nueva pregunta", preguntaCaptor.getValue().getEnunciado());
    }

    @Test
    void eliminar_Existente_DeberiaLlamarDeleteById() {
        doNothing().when(preguntaRepository).deleteById(1L);

        preguntaService.eliminar(1L);

        verify(preguntaRepository).deleteById(1L);
    }

    @Test
    void eliminar_NoExistente_DeberiaLlamarDeleteByIdSinError() {
        doNothing().when(preguntaRepository).deleteById(99L);

        preguntaService.eliminar(99L);

        verify(preguntaRepository).deleteById(99L);
    }
}