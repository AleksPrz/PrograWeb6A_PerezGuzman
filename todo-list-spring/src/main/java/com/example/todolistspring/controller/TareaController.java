package com.example.todolistspring.controller;
import com.example.todolistspring.models.Usuario;
import com.example.todolistspring.models.Tarea;
import com.example.todolistspring.service.TareaService;
import com.example.todolistspring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.todolistspring.service.TokenService;

import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
    private final TareaService tareaService;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public TareaController(TareaService tareaService, TokenService tokenService, UsuarioService usuarioService) {
        this.tareaService = tareaService;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Tarea>> obtenerTodas(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username;
        try {
            username = tokenService.getUsernameFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        Optional<Usuario> usuario = usuarioService.obtenerPorUsername(username);
        if (usuario.isEmpty()) { return ResponseEntity.notFound().build(); }

        return ResponseEntity.ok(tareaService.obtenerTodasPorUsuario(usuario.get()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username;
        try {
            username = tokenService.getUsernameFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        Optional<Tarea> tarea = tareaService.obtenerPorId(id);
        if (tarea.isEmpty()) { return ResponseEntity.notFound().build(); }

        Optional<Usuario> usuario = usuarioService.obtenerPorUsername(username);
        if (usuario.isEmpty()) { return ResponseEntity.notFound().build(); }

        // Retornar la tarea solo si coincide con el usuario
        if(usuario.get().getId() == tarea.get().getUsuario().getId()) {
            return ResponseEntity.ok(tarea.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    };

    @PostMapping
    public ResponseEntity<Tarea> crear(@RequestBody Tarea tarea, @RequestHeader("Authorization") String tokenHeader) {
        // Validar token
        String token = tokenHeader.replace("Bearer ", "");
        if(!tokenService.validateToken(token)) {
            return ResponseEntity.badRequest().build();
        }
        // Obtener usuario del token
        String username = tokenService.getUsernameFromToken(token);
        Optional<Usuario> usuario = usuarioService.obtenerPorUsername(username);

        if(usuario.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // Asignar usuario a la tarea
        tarea.setUsuario(usuario.get());

        return ResponseEntity.ok(tareaService.guardar(tarea));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @RequestBody Tarea tareaActualizada, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username;
        try {
            username = tokenService.getUsernameFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        try {
            if(!tareaService.verificarUsuario(id, username)) { throw new RuntimeException("Esta tarea no pertenece al usuario"); }
            Tarea tarea = tareaService.actualizar(id, tareaActualizada);
            return ResponseEntity.ok(tarea);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tarea> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username;
        try {
            username = tokenService.getUsernameFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        try {
            if(!tareaService.verificarUsuario(id, username)) { throw new RuntimeException("Esta tarea no pertenece al usuario"); }
            Tarea tarea = tareaService.actualizarParcial(id, campos);
            return ResponseEntity.ok(tarea);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username;
        try {
            username = tokenService.getUsernameFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        if (tareaService.obtenerPorId(id).isPresent() && tareaService.verificarUsuario(id, username)) { // ESTA COMPARACION NO ESTA CAPTURANDO LA EXCEPCION DE VERIFICAR USUARIO
            tareaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }
}
