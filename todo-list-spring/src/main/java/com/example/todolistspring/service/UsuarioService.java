package com.example.todolistspring.service;
import com.example.todolistspring.models.Usuario;
import com.example.todolistspring.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordService passwordService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordService = passwordService;
    }

    public Usuario guardar(Usuario usuario) {
        String newPassword = passwordService.encryptPassword(usuario.getPassword());
        usuario.setPassword(newPassword);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
