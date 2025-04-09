package com.example.todolistspring.repository;

import com.example.todolistspring.models.Tarea;
import com.example.todolistspring.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findAllByUsuario(Usuario usuario);
}
