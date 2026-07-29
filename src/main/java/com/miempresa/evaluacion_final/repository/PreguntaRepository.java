package com.miempresa.evaluacion_final.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miempresa.evaluacion_final.model.Pregunta;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    Page<Pregunta> findAllByOrderByTematicaIdAscIdAsc(Pageable pageable);

    List<Pregunta> findByTematicaId(Long tematicaId);

    @Query("SELECT p FROM Pregunta p WHERE (:tematicaId IS NULL OR p.tematica.id = :tematicaId) AND (:clase IS NULL OR TYPE(p) = :clase)")
    Page<Pregunta> findFiltered(@Param("tematicaId") Long tematicaId, @Param("clase") Class<? extends Pregunta> clase, Pageable pageable);
}