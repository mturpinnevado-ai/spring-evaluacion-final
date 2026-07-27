package com.miempresa.evaluacion_final.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreguntaDTO {

    private Long id;
    private String enunciado;
    private String respuesta;
    private Long tematicaId;
    private String tematicaNombre;
    private String tipo;
    private String tipoDescripcion;

    public static PreguntaDTO fromEntity(Pregunta pregunta) {
        return PreguntaDTO.builder()
                .id(pregunta.getId())
                .enunciado(pregunta.getEnunciado())
                .respuesta(pregunta.getRespuesta())
                .tematicaId(pregunta.getTematica() != null ? pregunta.getTematica().getId() : null)
                .tematicaNombre(pregunta.getTematica() != null ? pregunta.getTematica().getNombre() : null)
                .tipo(pregunta.getTipo())
                .tipoDescripcion(pregunta.getTipoDescripcion())
                .build();
    }
}