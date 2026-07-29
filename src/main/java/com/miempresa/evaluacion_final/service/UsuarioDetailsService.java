package com.miempresa.evaluacion_final.service;

import com.miempresa.evaluacion_final.model.Rol;
import com.miempresa.evaluacion_final.model.Usuario;
import com.miempresa.evaluacion_final.repository.IUsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioDetailsService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .map(SimpleGrantedAuthority::new)
                .collect(java.util.stream.Collectors.toList());

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(),
                true, true, true, authorities);
    }
}