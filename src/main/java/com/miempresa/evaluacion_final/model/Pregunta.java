package com.miempresa.evaluacion_final.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_pregunta", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ABIERTA")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{pregunta.enunciado.notblank}")
    @Size(min = 2, max = 150, message = "{pregunta.enunciado.size}")
    private String enunciado;

    private String respuesta;

    @NotNull(message = "{pregunta.tematica.notnull}")
    @ManyToOne
    private Tematica tematica;

    public Pregunta() {}

    public Pregunta(String enunciado, String respuesta, Tematica tematica) {
        this.enunciado = enunciado;
        this.respuesta = respuesta;
        this.tematica = tematica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Tematica getTematica() {
        return tematica;
    }

    public void setTematica(Tematica tematica) {
        this.tematica = tematica;
    }

    @Transient
    public String getTipo() {
        DiscriminatorValue dv = getClass().getAnnotation(DiscriminatorValue.class);
        return dv != null ? dv.value() : "ABIERTA";
    }

    @Transient
    public String getTipoDescripcion() {
        return switch (getTipo()) {
            case "V_F" -> "Verdadero/Falso";
            case "UNICA" -> "Selección Única";
            case "MULTIPLE" -> "Selección Múltiple";
            default -> "Abierta";
        };
    }
}