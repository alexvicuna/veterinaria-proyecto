package com.veterinaria.usuarios.service;

import com.veterinaria.usuarios.UsuariosException.EmailYaExisteException;
import com.veterinaria.usuarios.UsuariosException.UsuariosNotFoundException;
import com.veterinaria.usuarios.dto.UsuarioRequestDTO;
import com.veterinaria.usuarios.dto.UsuarioResponseDTO;

import com.veterinaria.usuarios.model.Rol;
import com.veterinaria.usuarios.model.Usuario;
import com.veterinaria.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuariosNotFoundException(id));
        return toResponse(usuario);
    }

    public UsuarioResponseDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuariosNotFoundException("Usuario no encontrado con email: " + email));
        return toResponse(usuario);
    }

    public List<UsuarioResponseDTO> listarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> listarActivos() {
        return usuarioRepository.findByActivo(true)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailYaExisteException(dto.getEmail());
        }
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .password(dto.getPassword()) // En producción se debe encriptar con BCrypt
                .rol(dto.getRol())
                .build();
        return toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuariosNotFoundException(id));

        // Verificar email duplicado solo si cambió
        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailYaExisteException(dto.getEmail());
        }

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        return toResponse(usuarioRepository.save(usuario));
    }

    public void desactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuariosNotFoundException(id));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuariosNotFoundException(id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .fechaCreacion(usuario.getFechaCreacion())
                .activo(usuario.getActivo())
                .build();
    }
}