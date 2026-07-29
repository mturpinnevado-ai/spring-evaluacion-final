package com.miempresa.evaluacion_final.model;

import java.util.ArrayList;
import java.util.List;

public class SesionJuego {

    private Long tematicaId;
    private List<Pregunta> preguntas;
    private int indiceActual;
    private int puntuacion;
    private List<RespuestaJuego> respuestas;

    public SesionJuego() {
        this.indiceActual = 0;
        this.puntuacion = 0;
        this.respuestas = new ArrayList<>();
    }

    public Long getTematicaId() {
        return tematicaId;
    }

    public void setTematicaId(Long tematicaId) {
        this.tematicaId = tematicaId;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    public void setIndiceActual(int indiceActual) {
        this.indiceActual = indiceActual;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<RespuestaJuego> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaJuego> respuestas) {
        this.respuestas = respuestas;
    }

    public Pregunta getPreguntaActual() {
        if (preguntas == null || indiceActual < 0 || indiceActual >= preguntas.size()) {
            return null;
        }
        return preguntas.get(indiceActual);
    }

    public boolean isTerminado() {
        return indiceActual >= preguntas.size();
    }

    public int getTotal() {
        return preguntas != null ? preguntas.size() : 0;
    }

    public static class RespuestaJuego {
        private Long preguntaId;
        private String enunciado;
        private String tipo;
        private String respuestaCorrecta;
        private String respuestaUsuario;
        private boolean correcta;

        public RespuestaJuego() {}

        public RespuestaJuego(Long preguntaId, String enunciado, String tipo,
                              String respuestaCorrecta, String respuestaUsuario, boolean correcta) {
            this.preguntaId = preguntaId;
            this.enunciado = enunciado;
            this.tipo = tipo;
            this.respuestaCorrecta = respuestaCorrecta;
            this.respuestaUsuario = respuestaUsuario;
            this.correcta = correcta;
        }

        public Long getPreguntaId() { return preguntaId; }
        public void setPreguntaId(Long preguntaId) { this.preguntaId = preguntaId; }
        public String getEnunciado() { return enunciado; }
        public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getRespuestaCorrecta() { return respuestaCorrecta; }
        public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }
        public String getRespuestaUsuario() { return respuestaUsuario; }
        public void setRespuestaUsuario(String respuestaUsuario) { this.respuestaUsuario = respuestaUsuario; }
        public boolean isCorrecta() { return correcta; }
        public void setCorrecta(boolean correcta) { this.correcta = correcta; }
    }
}