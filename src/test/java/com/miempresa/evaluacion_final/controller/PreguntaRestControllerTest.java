package com.miempresa.evaluacion_final.controller;

import com.miempresa.evaluacion_final.exception.PreguntaNoEncontradaException;
import com.miempresa.evaluacion_final.model.Pregunta;
import com.miempresa.evaluacion_final.model.PreguntaVerdaderoFalso;
import com.miempresa.evaluacion_final.model.Tematica;
import com.miempresa.evaluacion_final.repository.TematicaRepository;
import com.miempresa.evaluacion_final.service.IPreguntaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.SpringDataJacksonConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PreguntaRestControllerTest {

    @Mock
    private IPreguntaService preguntaService;

    @Mock
    private TematicaRepository tematicaRepository;

    @InjectMocks
    private PreguntaRestController controller;

    private MockMvc mockMvc;

    private final Tematica tematica = new Tematica("Java Básico");
    {
        tematica.setId(1L);
    }

    private final Pregunta preguntaAbierta = new Pregunta("¿Qué es Java?", "Un lenguaje", tematica);
    {
        preguntaAbierta.setId(1L);
    }

    private final PreguntaVerdaderoFalso preguntaVF = new PreguntaVerdaderoFalso("Java es OOP", true, tematica);
    {
        preguntaVF.setId(2L);
    }

    @ControllerAdvice
    static class TestControllerAdvice {

        @ExceptionHandler(PreguntaNoEncontradaException.class)
        public ResponseEntity<Void> handleNotFound() {
            return ResponseEntity.notFound().build();
        }
    }

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new SpringDataJacksonConfiguration().pageModule())
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new TestControllerAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void listar_DeberiaRetornarPaginaDeDTOs() throws Exception {
        Page<Pregunta> pagina = new PageImpl<>(List.of(preguntaAbierta, preguntaVF));
        when(preguntaService.listarFiltradas(any(), any(), any())).thenReturn(pagina);

        mockMvc.perform(get("/api/preguntas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].enunciado").value("¿Qué es Java?"))
                .andExpect(jsonPath("$.content[0].tipo").value("ABIERTA"))
                .andExpect(jsonPath("$.content[1].enunciado").value("Java es OOP"))
                .andExpect(jsonPath("$.content[1].tipo").value("V_F"));
    }

    @Test
    void listar_ConFiltros_DeberiaPasarParametrosAlServicio() throws Exception {
        Page<Pregunta> pagina = new PageImpl<>(List.of(preguntaAbierta));
        when(preguntaService.listarFiltradas(any(), any(), any())).thenReturn(pagina);

        mockMvc.perform(get("/api/preguntas")
                        .param("tematicaId", "1")
                        .param("tipo", "ABIERTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void obtenerPorId_Existente_DeberiaRetornarDTO() throws Exception {
        when(preguntaService.obtenerPorId(1L)).thenReturn(Optional.of(preguntaAbierta));

        mockMvc.perform(get("/api/preguntas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.enunciado").value("¿Qué es Java?"))
                .andExpect(jsonPath("$.tematicaId").value(1))
                .andExpect(jsonPath("$.tematicaNombre").value("Java Básico"))
                .andExpect(jsonPath("$.tipo").value("ABIERTA"));
    }

    @Test
    void obtenerPorId_NoExistente_DeberiaRetornar404() throws Exception {
        when(preguntaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/preguntas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_Abierta_DeberiaRetornar201() throws Exception {
        when(tematicaRepository.findById(1L)).thenReturn(Optional.of(tematica));
        when(preguntaService.guardar(any(Pregunta.class))).thenReturn(preguntaAbierta);

        String body = """
                {
                    "tipo": "ABIERTA",
                    "enunciado": "¿Qué es Java?",
                    "respuesta": "Un lenguaje",
                    "tematicaId": 1
                }
                """;

        mockMvc.perform(post("/api/preguntas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.enunciado").value("¿Qué es Java?"))
                .andExpect(jsonPath("$.tipo").value("ABIERTA"));
    }

    @Test
    void crear_VF_DeberiaRetornar201() throws Exception {
        when(tematicaRepository.findById(2L)).thenReturn(Optional.of(tematica));
        when(preguntaService.guardar(any(Pregunta.class))).thenReturn(preguntaVF);

        String body = """
                {
                    "tipo": "V_F",
                    "enunciado": "Java es OOP",
                    "esVerdadero": true,
                    "tematicaId": 2
                }
                """;

        mockMvc.perform(post("/api/preguntas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.enunciado").value("Java es OOP"))
                .andExpect(jsonPath("$.tipo").value("V_F"));
    }

    @Test
    void eliminar_Existente_DeberiaRetornar204() throws Exception {
        when(preguntaService.obtenerPorId(1L)).thenReturn(Optional.of(preguntaAbierta));

        mockMvc.perform(delete("/api/preguntas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_NoExistente_DeberiaRetornar404() throws Exception {
        when(preguntaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/preguntas/99"))
                .andExpect(status().isNotFound());
    }
}