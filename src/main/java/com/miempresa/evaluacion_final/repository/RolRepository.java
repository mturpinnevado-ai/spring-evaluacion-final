package com.miempresa.evaluacion_final.repository;

import com.miempresa.evaluacion_final.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}