package com.miempresa.evaluacion_final.service;

import com.miempresa.evaluacion_final.model.Rol;
import com.miempresa.evaluacion_final.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarTodos();
    Optional<Usuario> obtenerPorId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
    List<Rol> listarRoles();
}