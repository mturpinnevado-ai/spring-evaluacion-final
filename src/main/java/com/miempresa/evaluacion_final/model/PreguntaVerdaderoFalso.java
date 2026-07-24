package com.miempresa.evaluacion_final.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("V_F")
public class PreguntaVerdaderoFalso extends Pregunta {

    private boolean esVerdadero;

    public PreguntaVerdaderoFalso() {}

    public PreguntaVerdaderoFalso(String enunciado, boolean esVerdadero, Tematica tematica) {
        super(enunciado, null, tematica);
        this.esVerdadero = esVerdadero;
    }

    public boolean isEsVerdadero() {
        return esVerdadero;
    }

    public void setEsVerdadero(boolean esVerdadero) {
        this.esVerdadero = esVerdadero;
    }
}