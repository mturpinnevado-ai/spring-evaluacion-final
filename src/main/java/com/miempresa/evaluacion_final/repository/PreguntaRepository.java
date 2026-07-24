package com.miempresa.evaluacion_final.repository;

import com.miempresa.evaluacion_final.model.Pregunta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    Page<Pregunta> findAllByOrderByTematicaIdAscIdAsc(Pageable pageable);
}