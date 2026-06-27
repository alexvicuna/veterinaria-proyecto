package com.veterinaria.inventario.controller;

import com.veterinaria.inventario.dto.InventarioRequestDTO;
import com.veterinaria.inventario.dto.InventarioResponseDTO;
import com.veterinaria.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
@Tag(name = "Inventario", description = "Operaciones para la gestión de medicamentos e insumos veterinarios")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Operation(
            summary = "Crear un nuevo producto",
            description = "Registra un nuevo medicamento o insumo en el inventario de la veterinaria"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "idProducto": 1,
                                      "nombreProducto": "Amoxicilina",
                                      "descripcion": "Antibiótico de amplio espectro",
                                      "categoriaProducto": "MEDICAMENTO",
                                      "cantidadDisponible": 50
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "El nombre del producto es obligatorio" }
                                    """)))
    })
    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crearProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo producto",
                    required = true,
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "nombreProducto": "Amoxicilina",
                              "descripcion": "Antibiótico de amplio espectro",
                              "categoriaProducto": "MEDICAMENTO",
                              "cantidadDisponible": 50
                            }
                            """))
            )
            @Valid @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearProducto(dto));
    }


    @Operation(
            summary = "Obtener todos los productos",
            description = "Retorna la lista completa de productos del inventario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(inventarioService.obtenerTodos());
    }

    @Operation(
            summary = "Obtener producto por ID",
            description = "Busca y retorna un producto específico por su identificador único"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Producto no encontrado con el ID: 99" }
                                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> obtenerPorId(
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }


    @Operation(
            summary = "Buscar productos por nombre",
            description = "Retorna todos los productos cuyo nombre contenga el texto indicado (sin distinción de mayúsculas)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDTO.class)))
    })
    @GetMapping("/nombre/{nombreProducto}")
    public ResponseEntity<List<InventarioResponseDTO>> obtenerPorNombre(
            @Parameter(description = "Nombre o fragmento del nombre a buscar", example = "amoxi", required = true)
            @PathVariable String nombreProducto) {
        return ResponseEntity.ok(inventarioService.obtenerPorNombre(nombreProducto));
    }


    @Operation(
            summary = "Buscar productos por categoría",
            description = "Retorna todos los productos de una categoría específica. Categorías: MEDICAMENTO, VACUNA, INSUMO_CLINICO, ALIMENTO"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos de la categoría",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDTO.class)))
    })
    @GetMapping("/categoria/{categoriaProducto}")
    public ResponseEntity<List<InventarioResponseDTO>> obtenerPorCategoria(
            @Parameter(description = "Categoría del producto", example = "MEDICAMENTO", required = true)
            @PathVariable String categoriaProducto) {
        return ResponseEntity.ok(inventarioService.obtenerPorCategoria(categoriaProducto));
    }


    @Operation(
            summary = "Obtener productos con stock bajo",
            description = "Retorna todos los productos cuya cantidad disponible sea menor al valor indicado. Útil para alertas de reposición."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos con stock bajo",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "idProducto": 3,
                                        "nombreProducto": "Jeringa 5ml",
                                        "categoriaProducto": "INSUMO_CLINICO",
                                        "cantidadDisponible": 3
                                      }
                                    ]
                                    """)))
    })
    @GetMapping("/stock-bajo/{cantidad}")
    public ResponseEntity<List<InventarioResponseDTO>> obtenerStockBajo(
            @Parameter(description = "Cantidad mínima de referencia", example = "10", required = true)
            @PathVariable Integer cantidad) {
        return ResponseEntity.ok(inventarioService.obtenerStockBajo(cantidad));
    }



    @Operation(
            summary = "Actualizar un producto",
            description = "Actualiza los datos de un producto existente en el inventario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Producto no encontrado con el ID: 99" }
                                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.ok(inventarioService.actualizarProducto(id, dto));
    }

    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina permanentemente un producto del inventario por su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Producto no encontrado con el ID: 99" }
                                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        inventarioService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}