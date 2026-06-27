package com.veterinaria.veterinarios;

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
import com.veterinaria.veterinarios.dto.VeterinarioResponseDTO;
import com.veterinaria.veterinarios.model.Veterinario;
import com.veterinaria.veterinarios.repository.VeterinarioRepository;
import com.veterinaria.veterinarios.service.VeterinarioService;
import com.veterinaria.veterinarios.veterinariosException.VeterinariosNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Sin @SpringBootTest: no necesita MySQL ni Spring context
@ExtendWith(MockitoExtension.class)
class VeterinarioServiceTest {

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @InjectMocks
    private VeterinarioService veterinarioService;

    private Veterinario veterinarioMock;
    private VeterinarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        veterinarioMock = new Veterinario();
        veterinarioMock.setIdVeterinario(1L);
        veterinarioMock.setRutVet("12345678-9");
        veterinarioMock.setNombreVet("Carlos");
        veterinarioMock.setApellidoVet("Rojas");
        veterinarioMock.setEspecialidad("Cirugia");
        veterinarioMock.setTelefono("+56912345678");
        veterinarioMock.setCorreo("carlos.rojas@vet.cl");

        requestDTO = new VeterinarioRequestDTO(
                "12345678-9", "Carlos", "Rojas",
                "Cirugia", "+56912345678", "carlos.rojas@vet.cl"
        );
    }

    // PRUEBA 1: Registrar veterinario con RUT unico
    @Test
    void guardarVeterinario_rutNuevo_debeGuardarYRetornarDTO() {
        // Given
        when(veterinarioRepository.findByRutVet("12345678-9")).thenReturn(Optional.empty());
        when(veterinarioRepository.save(any(Veterinario.class))).thenReturn(veterinarioMock);

        // When
        VeterinarioResponseDTO resultado = veterinarioService.guardarVeterinario(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRutVet());
        assertEquals("Carlos", resultado.getNombreVet());
        assertEquals("Cirugia", resultado.getEspecialidad());
        verify(veterinarioRepository, times(1)).save(any(Veterinario.class));
    }

    // PRUEBA 2: Regla de negocio - RUT duplicado debe rechazarse
    @Test
    void guardarVeterinario_rutDuplicado_debeLanzarExcepcion() {
        // Given
        when(veterinarioRepository.findByRutVet("12345678-9"))
                .thenReturn(Optional.of(veterinarioMock));

        // When + Then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> veterinarioService.guardarVeterinario(requestDTO));

        assertTrue(ex.getMessage().contains("12345678-9"));
        verify(veterinarioRepository, never()).save(any());
    }

    // PRUEBA 3: Obtener veterinario por ID existente
    @Test
    void obtenerPorId_idExistente_debeRetornarVeterinario() {
        // Given
        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(veterinarioMock));

        // When
        VeterinarioResponseDTO resultado = veterinarioService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdVeterinario());
        assertEquals("Carlos", resultado.getNombreVet());
    }

    // PRUEBA 4: Obtener veterinario por ID inexistente lanza excepcion personalizada
    @Test
    void obtenerPorId_idInexistente_debeLanzarVeterinariosNotFoundException() {
        // Given
        when(veterinarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        VeterinariosNotFoundException ex = assertThrows(VeterinariosNotFoundException.class,
                () -> veterinarioService.obtenerPorId(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    // PRUEBA 5: Obtener todos los veterinarios
    @Test
    void obtenerTodos_debeRetornarListaCompleta() {
        // Given
        Veterinario segundo = new Veterinario();
        segundo.setIdVeterinario(2L);
        segundo.setRutVet("98765432-1");
        segundo.setNombreVet("Maria");
        segundo.setApellidoVet("Lopez");
        segundo.setEspecialidad("Dermatologia");
        segundo.setTelefono("+56987654321");
        segundo.setCorreo("maria@vet.cl");

        when(veterinarioRepository.findAll()).thenReturn(List.of(veterinarioMock, segundo));

        // When
        List<VeterinarioResponseDTO> resultado = veterinarioService.obtenerTodos();

        // Then
        assertEquals(2, resultado.size());
        verify(veterinarioRepository, times(1)).findAll();
    }

    // PRUEBA 6: Obtener veterinario por RUT existente
    @Test
    void obtenerPorRut_rutExistente_debeRetornarVeterinario() {
        // Given
        when(veterinarioRepository.findByRutVet("12345678-9"))
                .thenReturn(Optional.of(veterinarioMock));

        // When
        VeterinarioResponseDTO resultado = veterinarioService.obtenerPorRut("12345678-9");

        // Then
        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRutVet());
    }

    // PRUEBA 7: Obtener veterinario por RUT inexistente lanza excepcion
    @Test
    void obtenerPorRut_rutInexistente_debeLanzarExcepcion() {
        // Given
        when(veterinarioRepository.findByRutVet("00000000-0")).thenReturn(Optional.empty());

        // When + Then
        assertThrows(VeterinariosNotFoundException.class,
                () -> veterinarioService.obtenerPorRut("00000000-0"));
    }

    // PRUEBA 8: Buscar por especialidad valida retorna resultados
    @Test
    void obtenerPorEspecialidad_especialidadValida_debeRetornarVeterinarios() {
        // Given
        when(veterinarioRepository.findByEspecialidadContainingIgnoreCase("cirugia"))
                .thenReturn(List.of(veterinarioMock));

        // When
        List<VeterinarioResponseDTO> resultado =
                veterinarioService.obtenerPorEspecialidad("cirugia");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Cirugia", resultado.get(0).getEspecialidad());
    }

    // PRUEBA 9: Regla de negocio - especialidad vacia debe rechazarse
    @Test
    void obtenerPorEspecialidad_especialidadVacia_debeLanzarExcepcion() {
        // Given / When + Then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> veterinarioService.obtenerPorEspecialidad("  "));

        assertTrue(ex.getMessage().contains("vacia"));
        verify(veterinarioRepository, never()).findByEspecialidadContainingIgnoreCase(any());
    }

    // PRUEBA 10: Actualizar veterinario manteniendo mismo RUT
    @Test
    void actualizarVeterinario_mismoRut_debeActualizarCorrectamente() {
        // Given
        VeterinarioRequestDTO updateDTO = new VeterinarioRequestDTO(
                "12345678-9", "Carlos Alberto", "Rojas Silva",
                "Neurologia", "+56911111111", "carlos.nuevo@vet.cl"
        );

        Veterinario actualizado = new Veterinario();
        actualizado.setIdVeterinario(1L);
        actualizado.setRutVet("12345678-9");
        actualizado.setNombreVet("Carlos Alberto");
        actualizado.setApellidoVet("Rojas Silva");
        actualizado.setEspecialidad("Neurologia");
        actualizado.setTelefono("+56911111111");
        actualizado.setCorreo("carlos.nuevo@vet.cl");

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(veterinarioMock));
        when(veterinarioRepository.save(any(Veterinario.class))).thenReturn(actualizado);

        // When
        VeterinarioResponseDTO resultado = veterinarioService.actualizarVeterinario(1L, updateDTO);

        // Then
        assertEquals("Carlos Alberto", resultado.getNombreVet());
        assertEquals("Neurologia", resultado.getEspecialidad());
    }

    // PRUEBA 11: Eliminar veterinario existente
    @Test
    void eliminarVeterinario_idExistente_debeEliminarSinError() {
        // Given
        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(veterinarioMock));
        doNothing().when(veterinarioRepository).delete(veterinarioMock);

        // When + Then
        assertDoesNotThrow(() -> veterinarioService.eliminarVeterinario(1L));
        verify(veterinarioRepository, times(1)).delete(veterinarioMock);
    }

    // PRUEBA 12: Eliminar veterinario inexistente lanza excepcion
    @Test
    void eliminarVeterinario_idInexistente_debeLanzarExcepcion() {
        // Given
        when(veterinarioRepository.findById(50L)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(VeterinariosNotFoundException.class,
                () -> veterinarioService.eliminarVeterinario(50L));
        verify(veterinarioRepository, never()).delete(any());
    }
}