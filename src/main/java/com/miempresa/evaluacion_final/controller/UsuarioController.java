package com.miempresa.evaluacion_final.controller;

import com.miempresa.evaluacion_final.model.Rol;
import com.miempresa.evaluacion_final.model.Usuario;
import com.miempresa.evaluacion_final.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuario/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("todosRoles", usuarioService.listarRoles());
        return "usuario/form";
    }

    @PostMapping("/guardar")
    public String crear(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam(required = false) String email,
                        @RequestParam(defaultValue = "true") boolean enabled,
                        @RequestParam(required = false) List<Long> roles,
                        RedirectAttributes redirect) {
        List<Long> rolesIds = roles != null ? roles : List.of();
        List<Rol> rolesList = usuarioService.listarRoles().stream()
                .filter(r -> rolesIds.contains(r.getId()))
                .toList();
        Usuario usuario = Usuario.builder()
                .username(username)
                .password(password)
                .email(email)
                .enabled(enabled)
                .roles(rolesList)
                .build();
        usuarioService.guardar(usuario);
        redirect.addFlashAttribute("success", "Usuario creado correctamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        model.addAttribute("usuario", usuario);
        model.addAttribute("todosRoles", usuarioService.listarRoles());
        return "usuario/form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String username,
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false) String email,
                             @RequestParam(defaultValue = "true") boolean enabled,
                             @RequestParam(required = false) List<Long> roles,
                             RedirectAttributes redirect) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        usuario.setUsername(username);
        if (password != null && !password.isBlank()) {
            usuario.setPassword(password);
        }
        usuario.setEmail(email);
        usuario.setEnabled(enabled);
        List<Long> rolesIds = roles != null ? roles : List.of();
        List<Rol> rolesList = usuarioService.listarRoles().stream()
                .filter(r -> rolesIds.contains(r.getId()))
                .toList();
        usuario.setRoles(rolesList);
        usuarioService.guardar(usuario);
        redirect.addFlashAttribute("success", "Usuario actualizado correctamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        usuarioService.eliminar(id);
        redirect.addFlashAttribute("success", "Usuario eliminado correctamente");
        return "redirect:/usuarios";
    }
}