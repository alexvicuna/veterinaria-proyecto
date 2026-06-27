package com.veterinaria.usuarios.service;

import com.veterinaria.usuarios.UsuariosException.EmailYaExisteException;
import com.veterinaria.usuarios.UsuariosException.UsuariosNotFoundException;
import com.veterinaria.usuarios.dto.UsuarioRequestDTO;
import com.veterinaria.usuarios.dto.UsuarioResponseDTO;
import com.veterinaria.usuarios.model.Rol;
import com.veterinaria.usuarios.model.Usuario;
import com.veterinaria.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - UsuarioService")
class UsuariosServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    // ──────────────────────────────────────────────
    // Datos de prueba reutilizables
    // ──────────────────────────────────────────────

    private Usuario usuarioGuardado;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        usuarioGuardado = Usuario.builder()
                .idUsuario(1L)
                .nombre("Carlos")
                .apellido("Pérez")
                .email("carlos@mail.com")
                .password("123456")
                .rol(Rol.CLIENTE)
                .fechaCreacion(LocalDateTime.now())
                .activo(true)
                .build();

        requestDTO = new UsuarioRequestDTO();
        requestDTO.setNombre("Carlos");
        requestDTO.setApellido("Pérez");
        requestDTO.setEmail("carlos@mail.com");
        requestDTO.setPassword("123456");
        requestDTO.setRol(Rol.CLIENTE);
    }

    // ══════════════════════════════════════════════
    // listarTodos
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("listarTodos - debe retornar lista con todos los usuarios")
    void listarTodos_cuandoExistenUsuarios_debeRetornarLista() {
        // Given
        Usuario otroUsuario = Usuario.builder()
                .idUsuario(2L)
                .nombre("Ana")
                .apellido("González")
                .email("ana@mail.com")
                .password("654321")
                .rol(Rol.VETERINARIO)
                .fechaCreacion(LocalDateTime.now())
                .activo(true)
                .build();

        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioGuardado, otroUsuario));

        // When
        List<UsuarioResponseDTO> resultado = usuarioService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNombre());
        assertEquals("Ana", resultado.get(1).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarTodos - debe retornar lista vacía cuando no hay usuarios")
    void listarTodos_cuandoNoExistenUsuarios_debeRetornarListaVacia() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UsuarioResponseDTO> resultado = usuarioService.listarTodos();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // ══════════════════════════════════════════════
    // obtenerPorId
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPorId - debe retornar el usuario cuando existe el ID")
    void obtenerPorId_cuandoExisteId_debeRetornarUsuario() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioGuardado));

        // When
        UsuarioResponseDTO resultado = usuarioService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("carlos@mail.com", resultado.getEmail());
        assertEquals(Rol.CLIENTE, resultado.getRol());
        assertTrue(resultado.getActivo());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - debe lanzar UsuariosNotFoundException cuando el ID no existe")
    void obtenerPorId_cuandoNoExisteId_debeLanzarExcepcion() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        UsuariosNotFoundException excepcion = assertThrows(
                UsuariosNotFoundException.class,
                () -> usuarioService.obtenerPorId(99L)
        );

        assertTrue(excepcion.getMessage().contains("99"));
        verify(usuarioRepository, times(1)).findById(99L);
    }

    // ══════════════════════════════════════════════
    // obtenerPorEmail
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPorEmail - debe retornar el usuario cuando existe el email")
    void obtenerPorEmail_cuandoExisteEmail_debeRetornarUsuario() {
        // Given
        when(usuarioRepository.findByEmail("carlos@mail.com"))
                .thenReturn(Optional.of(usuarioGuardado));

        // When
        UsuarioResponseDTO resultado = usuarioService.obtenerPorEmail("carlos@mail.com");

        // Then
        assertNotNull(resultado);
        assertEquals("carlos@mail.com", resultado.getEmail());
        assertEquals("Carlos", resultado.getNombre());
    }

    @Test
    @DisplayName("obtenerPorEmail - debe lanzar excepción cuando el email no existe")
    void obtenerPorEmail_cuandoNoExisteEmail_debeLanzarExcepcion() {
        // Given
        when(usuarioRepository.findByEmail("noexiste@mail.com"))
                .thenReturn(Optional.empty());

        // When / Then
        UsuariosNotFoundException excepcion = assertThrows(
                UsuariosNotFoundException.class,
                () -> usuarioService.obtenerPorEmail("noexiste@mail.com")
        );

        assertTrue(excepcion.getMessage().contains("noexiste@mail.com"));
    }

    // ══════════════════════════════════════════════
    // listarPorRol
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("listarPorRol - debe retornar usuarios del rol indicado")
    void listarPorRol_cuandoExistenUsuarios_debeRetornarLista() {
        // Given
        when(usuarioRepository.findByRol(Rol.CLIENTE))
                .thenReturn(List.of(usuarioGuardado));

        // When
        List<UsuarioResponseDTO> resultado = usuarioService.listarPorRol(Rol.CLIENTE);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(Rol.CLIENTE, resultado.get(0).getRol());
    }

    @Test
    @DisplayName("listarPorRol - debe retornar vacío si no hay usuarios con ese rol")
    void listarPorRol_cuandoNoHayUsuarios_debeRetornarListaVacia() {
        // Given
        when(usuarioRepository.findByRol(Rol.ADMINISTRADOR))
                .thenReturn(Collections.emptyList());

        // When
        assertTrue(usuarioService.listarPorRol(Rol.ADMINISTRADOR).isEmpty());
    }

    // ══════════════════════════════════════════════
    // listarActivos
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("listarActivos - debe retornar solo usuarios con activo=true")
    void listarActivos_cuandoExistenUsuariosActivos_debeRetornarLista() {
        // Given
        when(usuarioRepository.findByActivo(true)).thenReturn(List.of(usuarioGuardado));

        // When
        List<UsuarioResponseDTO> resultado = usuarioService.listarActivos();

        // Then
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
        verify(usuarioRepository, times(1)).findByActivo(true);
    }

    @Test
    @DisplayName("listarActivos - debe retornar vacío si no hay usuarios activos")
    void listarActivos_cuandoNoHayActivos_debeRetornarListaVacia() {
        // Given
        when(usuarioRepository.findByActivo(true)).thenReturn(Collections.emptyList());

        // When
        assertTrue(usuarioService.listarActivos().isEmpty());
    }

    // ══════════════════════════════════════════════
    // crear
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("crear - debe guardar y retornar el usuario cuando el email no existe")
    void crear_cuandoEmailNoExiste_debeCrearUsuario() {
        // Given
        when(usuarioRepository.existsByEmail("carlos@mail.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // When
        UsuarioResponseDTO resultado = usuarioService.crear(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("carlos@mail.com", resultado.getEmail());
        assertEquals(Rol.CLIENTE, resultado.getRol());

        verify(usuarioRepository, times(1)).existsByEmail("carlos@mail.com");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("crear - debe lanzar EmailYaExisteException cuando el email ya está registrado")
    void crear_cuandoEmailYaExiste_debeLanzarExcepcion() {
        // Given - regla de negocio: no se permiten emails duplicados
        when(usuarioRepository.existsByEmail("carlos@mail.com")).thenReturn(true);

        // When / Then
        EmailYaExisteException excepcion = assertThrows(
                EmailYaExisteException.class,
                () -> usuarioService.crear(requestDTO)
        );

        assertTrue(excepcion.getMessage().contains("carlos@mail.com"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("crear - debe asignar activo=true automáticamente al crear usuario")
    void crear_debeAsignarActivoTrue() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // When
        UsuarioResponseDTO resultado = usuarioService.crear(requestDTO);

        // Then - el usuario recién creado debe estar activo
        assertTrue(resultado.getActivo());
    }

    // ══════════════════════════════════════════════
    // actualizar
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("actualizar - debe modificar los datos cuando el usuario existe")
    void actualizar_cuandoExisteUsuario_debeActualizarYRetornar() {
        // Given
        UsuarioRequestDTO requestActualizar = new UsuarioRequestDTO();
        requestActualizar.setNombre("Carlos Actualizado");
        requestActualizar.setApellido("Pérez");
        requestActualizar.setEmail("carlos@mail.com"); // mismo email
        requestActualizar.setPassword("nuevapass");
        requestActualizar.setRol(Rol.VETERINARIO);

        Usuario usuarioActualizado = Usuario.builder()
                .idUsuario(1L)
                .nombre("Carlos Actualizado")
                .apellido("Pérez")
                .email("carlos@mail.com")
                .password("nuevapass")
                .rol(Rol.VETERINARIO)
                .fechaCreacion(LocalDateTime.now())
                .activo(true)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioGuardado));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // When
        UsuarioResponseDTO resultado = usuarioService.actualizar(1L, requestActualizar);

        // Then
        assertEquals("Carlos Actualizado", resultado.getNombre());
        assertEquals(Rol.VETERINARIO, resultado.getRol());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("actualizar - debe lanzar excepción cuando el usuario no existe")
    void actualizar_cuandoNoExisteUsuario_debeLanzarExcepcion() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UsuariosNotFoundException.class,
                () -> usuarioService.actualizar(99L, requestDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("actualizar - debe lanzar EmailYaExisteException cuando el nuevo email ya pertenece a otro usuario")
    void actualizar_cuandoNuevoEmailYaExiste_debeLanzarExcepcion() {
        // Given - intenta cambiar el email a uno que ya usa otro usuario
        requestDTO.setEmail("otro@mail.com");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioGuardado));
        when(usuarioRepository.existsByEmail("otro@mail.com")).thenReturn(true);

        // When / Then
        assertThrows(EmailYaExisteException.class,
                () -> usuarioService.actualizar(1L, requestDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ══════════════════════════════════════════════
    // desactivar
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("desactivar - debe establecer activo=false cuando el usuario existe")
    void desactivar_cuandoExisteUsuario_debeDesactivar() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioGuardado));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // When
        usuarioService.desactivar(1L);

        // Then - verifica que se guardó con activo=false
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(argThat(u -> !u.getActivo()));
    }

    @Test
    @DisplayName("desactivar - debe lanzar excepción cuando el usuario no existe")
    void desactivar_cuandoNoExisteUsuario_debeLanzarExcepcion() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UsuariosNotFoundException.class,
                () -> usuarioService.desactivar(99L));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ══════════════════════════════════════════════
    // eliminar
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("eliminar - debe eliminar el usuario cuando existe el ID")
    void eliminar_cuandoExisteId_debeEliminar() {
        // Given
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        // When
        usuarioService.eliminar(1L);

        // Then
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - debe lanzar excepción cuando el ID no existe")
    void eliminar_cuandoNoExisteId_debeLanzarExcepcion() {
        // Given
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        // When / Then
        UsuariosNotFoundException excepcion = assertThrows(
                UsuariosNotFoundException.class,
                () -> usuarioService.eliminar(99L)
        );

        assertTrue(excepcion.getMessage().contains("99"));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}