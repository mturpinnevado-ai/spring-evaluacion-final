package com.miempresa.evaluacion_final.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MULTIPLE")
public class PreguntaSeleccionMultiple extends Pregunta {

    private String opciones;
    private String opcionesCorrectas;

    public PreguntaSeleccionMultiple() {}

    public PreguntaSeleccionMultiple(String enunciado, String opciones, String opcionesCorrectas, Tematica tematica) {
        super(enunciado, null, tematica);
        this.opciones = opciones;
        this.opcionesCorrectas = opcionesCorrectas;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    public String getOpcionesCorrectas() {
        return opcionesCorrectas;
    }

    public void setOpcionesCorrectas(String opcionesCorrectas) {
        this.opcionesCorrectas = opcionesCorrectas;
    }
}