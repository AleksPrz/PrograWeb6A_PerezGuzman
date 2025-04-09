package com.example.todolistspring.service;

import com.example.todolistspring.models.Tarea;
import com.example.todolistspring.models.Usuario;
import com.example.todolistspring.repository.TareaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;
    private final UsuarioService usuarioService;

    public TareaService(TareaRepository tareaRepository, UsuarioService usuarioService) {
        this.tareaRepository = tareaRepository;
        this.usuarioService = usuarioService;
    }

    public List<Tarea> obtenerTodas() {
        return tareaRepository.findAll();
    }

    public List<Tarea> obtenerTodasPorUsuario(Usuario usuario) {
        return tareaRepository.findAllByUsuario(usuario);
    }

    public Optional<Tarea> obtenerPorId(Long id) {
        return tareaRepository.findById(id);
    }

    public Tarea guardar(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public Tarea actualizar(Long id, Tarea tareaActualizada) {
        return tareaRepository.findById(id).map(tarea -> {
            tarea.setTitulo(tareaActualizada.getTitulo());
            tarea.setDescripcion(tareaActualizada.getDescripcion());
            tarea.setCompletada(tareaActualizada.isCompletada());

            return tareaRepository.save(tarea);
        }).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    public Tarea actualizarParcial(Long id, Map<String, Object> campos) {
        return tareaRepository.findById(id).map(tarea -> {
            if (campos.containsKey("titulo")) {
                tarea.setTitulo((String) campos.get("titulo"));
            }

            if (campos.containsKey("descripcion")) {
                tarea.setDescripcion((String) campos.get("descripcion"));
            }

            if (campos.containsKey("completada")) {
                tarea.setCompletada((boolean) campos.get("completada"));
            }
            return tareaRepository.save(tarea);
        }).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    public void eliminar(Long id) {
        tareaRepository.deleteById(id);
    }

    public boolean verificarUsuario(Long idTarea, String username) {
        Optional<Tarea> tarea = tareaRepository.findById(idTarea);
        if(tarea.isEmpty()) { throw new RuntimeException("Tarea no encontrada"); }

        Optional<Usuario> usuario = usuarioService.obtenerPorUsername(username);
        if (usuario.isEmpty()) { throw new RuntimeException("Usuario no encontrado"); }

        return usuario.get().getId() == tarea.get().getUsuario().getId();
    }
}
