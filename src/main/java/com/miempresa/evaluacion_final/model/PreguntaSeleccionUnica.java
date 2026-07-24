package com.miempresa.evaluacion_final.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("UNICA")
public class PreguntaSeleccionUnica extends Pregunta {

    private String opciones;
    private String opcionCorrecta;

    public PreguntaSeleccionUnica() {}

    public PreguntaSeleccionUnica(String enunciado, String opciones, String opcionCorrecta, Tematica tematica) {
        super(enunciado, null, tematica);
        this.opciones = opciones;
        this.opcionCorrecta = opcionCorrecta;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    public String getOpcionCorrecta() {
        return opcionCorrecta;
    }

    public void setOpcionCorrecta(String opcionCorrecta) {
        this.opcionCorrecta = opcionCorrecta;
    }
}