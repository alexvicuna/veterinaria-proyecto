package com.veterinaria.veterinarios.controller;

<<<<<<< HEAD

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
=======
import com.veterinaria.veterinarios.dto.VeterinarioResponseDTO;
>>>>>>> 0429cfed3641891bf219397071c83ecf49cf9344
import com.veterinaria.veterinarios.service.VeterinarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
<<<<<<< HEAD
@RequestMapping("/api/veterinarios") // URL base para Postman
public class VeterinarioController {

    @Autowired
    private VeterinarioService iVeterinarioService;


    @PostMapping
    public ResponseEntity<VeterinarioRenponseDTO> crearVeterinario(@Valid @RequestBody VeterinarioRequestDTO dto) {
        VeterinarioRenponseDTO nuevo = iVeterinarioService.guardarVeterinario(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VeterinarioRenponseDTO>> listarVeterinarios() {
        return ResponseEntity.ok(iVeterinarioService.obtenerTodos());
=======
@RequestMapping("/api/v1/veterinarios")
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @PostMapping
    public ResponseEntity<VeterinarioResponseDTO> registrarVeterinario(@Valid @RequestBody VeterinarioRequestDTO dto) {
        return new ResponseEntity<>(veterinarioService.registrarVeterinario(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VeterinarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(veterinarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.obtenerPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<VeterinarioResponseDTO> obtenerPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(veterinarioService.obtenerPorRut(rut));
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<VeterinarioResponseDTO>> obtenerPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(veterinarioService.obtenerPorEspecialidad(especialidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDTO> actualizarVeterinario(@PathVariable Long id,
                                                                        @Valid @RequestBody VeterinarioRequestDTO dto) {
        return ResponseEntity.ok(veterinarioService.actualizarVeterinario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVeterinario(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
        return ResponseEntity.noContent().build();
>>>>>>> 0429cfed3641891bf219397071c83ecf49cf9344
    }
}
