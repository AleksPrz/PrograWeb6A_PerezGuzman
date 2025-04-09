package com.example.todolistspring.controller;
import com.example.todolistspring.models.Usuario;
import com.example.todolistspring.service.PasswordService;
import com.example.todolistspring.service.TokenService;
import com.example.todolistspring.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final PasswordService passwordService;
    private final TokenService tokenService;

    public AuthController(UsuarioService usuarioService, PasswordService passwordService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        try {
            usuarioService.guardar(usuario);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> campos) {
        String email = campos.get("email");
        String password = campos.get("password");
        Optional<Usuario> usuario = usuarioService.obtenerPorEmail(email);
        Map<String, String> response = new HashMap<>();

        if (usuario.isEmpty()) {
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.badRequest().body(response);
        }

        if (passwordService.verifyPassword(password, usuario.get().getPassword())) {
            String token = tokenService.generateToken(usuario.get().getUsername());
            response.put("message", "Se ha autenticado el usuario");
            response.put("token", token);
            return ResponseEntity.ok(response);

        } else {
            response.put("message", "Contraseña incorrecta");
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String tokenHeader) {
        // Validar token
        String token = tokenHeader.replace("Bearer ", "");
        if(!tokenService.validateToken(token)) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok("Sesión finalizada");
    }


}
