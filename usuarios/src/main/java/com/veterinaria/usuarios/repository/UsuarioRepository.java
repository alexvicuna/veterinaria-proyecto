package com.veterinaria.usuarios.repository;

import com.veterinaria.usuarios.model.Rol;
import com.veterinaria.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByActivo(Boolean activo);

    boolean existsByEmail(String email);
}