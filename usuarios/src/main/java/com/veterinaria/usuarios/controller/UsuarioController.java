package com.veterinaria.usuarios.controller;

import com.veterinaria.usuarios.dto.UsuarioRequestDTO;
import com.veterinaria.usuarios.dto.UsuarioResponseDTO;
import com.veterinaria.usuarios.model.Rol;
import com.veterinaria.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones para la gestión de usuarios del sistema veterinario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ──────────────────────────────────────────────
    // GET /api/v1/usuarios
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Listar todos los usuarios",
            description = "Retorna la lista completa de usuarios registrados en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "idUsuario": 1,
                                        "nombre": "Carlos",
                                        "apellido": "Pérez",
                                        "email": "carlos@mail.com",
                                        "rol": "CLIENTE",
                                        "fechaCreacion": "2026-07-01T10:00:00",
                                        "activo": true
                                      }
                                    ]
                                    """)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/usuarios/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Busca y retorna un usuario específico por su identificador único"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Usuario no encontrado con ID: 99" }
                                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/usuarios/email/{email}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener usuario por email",
            description = "Busca y retorna un usuario por su dirección de correo electrónico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Usuario no encontrado con email: noexiste@mail.com" }
                                    """)))
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorEmail(
            @Parameter(description = "Email del usuario", example = "carlos@mail.com", required = true)
            @PathVariable String email) {
        return ResponseEntity.ok(usuarioService.obtenerPorEmail(email));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/usuarios/rol/{rol}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Listar usuarios por rol",
            description = "Retorna todos los usuarios que tengan el rol indicado. Roles disponibles: ADMINISTRADOR, VETERINARIO, CLIENTE"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios con el rol indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)))
    })
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioResponseDTO>> listarPorRol(
            @Parameter(description = "Rol a filtrar", example = "CLIENTE", required = true)
            @PathVariable Rol rol) {
        return ResponseEntity.ok(usuarioService.listarPorRol(rol));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/usuarios/activos
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Listar usuarios activos",
            description = "Retorna todos los usuarios con estado activo=true"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios activos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)))
    })
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarActivos() {
        return ResponseEntity.ok(usuarioService.listarActivos());
    }

    // ──────────────────────────────────────────────
    // POST /api/v1/usuarios
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema. No se permiten emails duplicados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "idUsuario": 1,
                                      "nombre": "Carlos",
                                      "apellido": "Pérez",
                                      "email": "carlos@mail.com",
                                      "rol": "CLIENTE",
                                      "fechaCreacion": "2026-07-01T10:00:00",
                                      "activo": true
                                    }
                                    """))),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Ya existe un usuario registrado con el email: carlos@mail.com" }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "fields": {
                                        "email": "El email no tiene un formato válido",
                                        "password": "La contraseña debe tener al menos 6 caracteres"
                                      }
                                    }
                                    """)))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario",
                    required = true,
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "nombre": "Carlos",
                              "apellido": "Pérez",
                              "email": "carlos@mail.com",
                              "password": "123456",
                              "rol": "CLIENTE"
                            }
                            """))
            )
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    // ──────────────────────────────────────────────
    // PUT /api/v1/usuarios/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Actualizar un usuario",
            description = "Actualiza los datos de un usuario existente. Si se cambia el email, no debe pertenecer a otro usuario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Usuario no encontrado con ID: 99" }
                                    """))),
            @ApiResponse(responseCode = "409", description = "El nuevo email ya está en uso",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Ya existe un usuario registrado con el email: otro@mail.com" }
                                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @Parameter(description = "ID del usuario a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    // ──────────────────────────────────────────────
    // PATCH /api/v1/usuarios/{id}/desactivar
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Desactivar un usuario",
            description = "Establece el estado del usuario como inactivo (activo=false) sin eliminarlo del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario desactivado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Usuario no encontrado con ID: 99" }
                                    """)))
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del usuario a desactivar", example = "1", required = true)
            @PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // ──────────────────────────────────────────────
    // DELETE /api/v1/usuarios/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Eliminar un usuario",
            description = "Elimina permanentemente un usuario del sistema. Para desactivarlo sin eliminarlo usar PATCH /desactivar"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Usuario no encontrado con ID: 99" }
                                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del usuario a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}