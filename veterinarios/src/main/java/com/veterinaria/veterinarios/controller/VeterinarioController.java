package com.veterinaria.veterinarios.controller;

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
import com.veterinaria.veterinarios.dto.VeterinarioResponseDTO;
import com.veterinaria.veterinarios.service.VeterinarioService;
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
@RequestMapping("/api/v1/veterinarios")
@RequiredArgsConstructor
@Tag(name = "Veterinarios", description = "Gestion de veterinarios del sistema")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @Operation(
            summary = "Registrar un veterinario",
            description = "Crea un nuevo veterinario en el sistema. El RUT debe ser unico y con formato valido (ej: 12345678-9)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Veterinario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = VeterinarioResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "idVeterinario": 1,
                      "rutVet": "12345678-9",
                      "nombreVet": "Carlos",
                      "apellidoVet": "Rojas",
                      "especialidad": "Cirugia",
                      "telefono": "+56912345678",
                      "correo": "carlos.rojas@vet.cl"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o RUT duplicado",
                    content = @Content(examples = @ExampleObject(
                            value = "{\"error\": \"Ya existe un veterinario registrado con el RUT: 12345678-9\"}")))
    })
    @PostMapping
    public ResponseEntity<VeterinarioResponseDTO> registrarVeterinario(
            @Valid @RequestBody VeterinarioRequestDTO dto) {
        return new ResponseEntity<>(veterinarioService.guardarVeterinario(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar todos los veterinarios",
            description = "Retorna la lista completa de veterinarios registrados"
    )
    @ApiResponse(responseCode = "200", description = "Lista de veterinarios",
            content = @Content(schema = @Schema(implementation = VeterinarioResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<VeterinarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(veterinarioService.obtenerTodos());
    }

    @Operation(
            summary = "Obtener veterinario por ID",
            description = "Retorna el detalle de un veterinario segun su identificador unico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veterinario encontrado",
                    content = @Content(schema = @Schema(implementation = VeterinarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado",
                    content = @Content(examples = @ExampleObject(
                            value = "{\"error\": \"No se encontro un veterinario con el ID: 99\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDTO> obtenerPorId(
            @Parameter(description = "ID del veterinario", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.obtenerPorId(id));
    }

    @Operation(
            summary = "Obtener veterinario por RUT",
            description = "Busca un veterinario por su RUT unico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veterinario encontrado"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<VeterinarioResponseDTO> obtenerPorRut(
            @Parameter(description = "RUT del veterinario (formato: 12345678-9)", required = true, example = "12345678-9")
            @PathVariable String rut) {
        return ResponseEntity.ok(veterinarioService.obtenerPorRut(rut));
    }

    @Operation(
            summary = "Buscar veterinarios por especialidad",
            description = "Retorna todos los veterinarios que tengan la especialidad indicada (busqueda parcial, sin distinguir mayusculas)"
    )
    @ApiResponse(responseCode = "200", description = "Veterinarios encontrados con esa especialidad")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<VeterinarioResponseDTO>> obtenerPorEspecialidad(
            @Parameter(description = "Especialidad a buscar", required = true, example = "Cirugia")
            @PathVariable String especialidad) {
        return ResponseEntity.ok(veterinarioService.obtenerPorEspecialidad(especialidad));
    }

    @Operation(
            summary = "Actualizar veterinario",
            description = "Modifica los datos de un veterinario existente. Si cambia el RUT, se valida que no este en uso"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veterinario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = VeterinarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado"),
            @ApiResponse(responseCode = "400", description = "RUT duplicado u otros datos invalidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDTO> actualizarVeterinario(
            @Parameter(description = "ID del veterinario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody VeterinarioRequestDTO dto) {
        return ResponseEntity.ok(veterinarioService.actualizarVeterinario(id, dto));
    }

    @Operation(
            summary = "Eliminar veterinario",
            description = "Elimina de forma permanente un veterinario segun su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Veterinario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVeterinario(
            @Parameter(description = "ID del veterinario a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
        return ResponseEntity.noContent().build();
    }
}