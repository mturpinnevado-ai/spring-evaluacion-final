package com.miempresa.evaluacion_final.exception;

public class PreguntaNoEncontradaException extends RuntimeException {

    public PreguntaNoEncontradaException(Long id) {
        super("Pregunta no encontrada: " + id);
    }
}