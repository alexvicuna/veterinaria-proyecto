package com.veterinaria.duenos;

import com.veterinaria.duenos.client.MascotaClient;
import com.veterinaria.duenos.dto.DuenoRequestDTO;
import com.veterinaria.duenos.dto.DuenoResponseDTO;
import com.veterinaria.duenos.dto.MascotaDTO;
import com.veterinaria.duenos.duenoException.DuenoNotFoundException;
import com.veterinaria.duenos.model.Dueno;
import com.veterinaria.duenos.repository.DuenoRepository;
import com.veterinaria.duenos.service.DuenoService;
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

@ExtendWith(MockitoExtension.class)
class DuenoServiceTest {

    @Mock
    private DuenoRepository duenoRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private DuenoService duenoService;

    private Dueno dueno;
    private DuenoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        dueno = new Dueno();
        dueno.setIdDueno(1L);
        dueno.setRut("12345678-9");
        dueno.setNombre("Juan");
        dueno.setApellido("Pérez");
        dueno.setTelefono("912345678");
        dueno.setCorreo("juan@correo.com");

        requestDTO = new DuenoRequestDTO();
        requestDTO.setRut("12345678-9");
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Pérez");
        requestDTO.setTelefono("912345678");
        requestDTO.setCorreo("juan@correo.com");
    }

    // ─── registrarDueno ───────────────────────────────────────────────────────

    @Test
    void registrarDueno_cuandoDatosValidos_debeRetornarDuenoGuardado() {
        // Given
        when(duenoRepository.save(any(Dueno.class))).thenReturn(dueno);
        when(mascotaClient.obtenerMascotasPorDueno(1L)).thenReturn(List.of());

        // When
        DuenoResponseDTO resultado = duenoService.registrarDueno(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Pérez", resultado.getApellido());
        verify(duenoRepository, times(1)).save(any(Dueno.class));
    }

    @Test
    void registrarDueno_cuandoSeGuarda_debeLlamarAlRepositorio() {
        // Given
        when(duenoRepository.save(any(Dueno.class))).thenReturn(dueno);
        when(mascotaClient.obtenerMascotasPorDueno(anyLong())).thenReturn(List.of());

        // When
        duenoService.registrarDueno(requestDTO);

        // Then
        verify(duenoRepository, times(1)).save(any(Dueno.class));
    }

    // ─── obtenerTodos ─────────────────────────────────────────────────────────

    @Test
    void obtenerTodos_cuandoExistenDuenos_debeRetornarLista() {
        // Given
        Dueno dueno2 = new Dueno();
        dueno2.setIdDueno(2L);
        dueno2.setNombre("María");
        when(duenoRepository.findAll()).thenReturn(List.of(dueno, dueno2));
        when(mascotaClient.obtenerMascotasPorDueno(anyLong())).thenReturn(List.of());

        // When
        List<DuenoResponseDTO> resultado = duenoService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(duenoRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodos_cuandoNoHayDuenos_debeRetornarListaVacia() {
        // Given
        when(duenoRepository.findAll()).thenReturn(List.of());

        // When
        List<DuenoResponseDTO> resultado = duenoService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─── obtenerPorId ─────────────────────────────────────────────────────────

    @Test
    void obtenerPorId_cuandoExisteElDueno_debeRetornarDueno() {
        // Given
        when(duenoRepository.findById(1L)).thenReturn(Optional.of(dueno));
        when(mascotaClient.obtenerMascotasPorDueno(1L)).thenReturn(List.of());

        // When
        DuenoResponseDTO resultado = duenoService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdDueno());
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarExcepcion() {
        // Given
        when(duenoRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> duenoService.obtenerPorId(99L));
        verify(duenoRepository, times(1)).findById(99L);
    }

    // ─── obtenerPorRut ────────────────────────────────────────────────────────

    @Test
    void obtenerPorRut_cuandoExisteElRut_debeRetornarDueno() {
        // Given
        when(duenoRepository.findByRut("12345678-9")).thenReturn(Optional.of(dueno));
        when(mascotaClient.obtenerMascotasPorDueno(1L)).thenReturn(List.of());

        // When
        DuenoResponseDTO resultado = duenoService.obtenerPorRut("12345678-9");

        // Then
        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
    }

    @Test
    void obtenerPorRut_cuandoNoExisteElRut_debeLanzarDuenoNotFoundException() {
        // Given
        when(duenoRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        // When / Then
        assertThrows(DuenoNotFoundException.class, () -> duenoService.obtenerPorRut("99999999-9"));
    }

    // ─── actualizarDueno ──────────────────────────────────────────────────────

    @Test
    void actualizarDueno_cuandoExiste_debeActualizarYRetornar() {
        // Given
        requestDTO.setNombre("Juan Actualizado");
        Dueno duenoActualizado = new Dueno();
        duenoActualizado.setIdDueno(1L);
        duenoActualizado.setNombre("Juan Actualizado");
        duenoActualizado.setRut("12345678-9");
        duenoActualizado.setApellido("Pérez");
        duenoActualizado.setTelefono("912345678");
        duenoActualizado.setCorreo("juan@correo.com");

        when(duenoRepository.findById(1L)).thenReturn(Optional.of(dueno));
        when(duenoRepository.save(any(Dueno.class))).thenReturn(duenoActualizado);
        when(mascotaClient.obtenerMascotasPorDueno(anyLong())).thenReturn(List.of());

        // When
        DuenoResponseDTO resultado = duenoService.actualizarDueno(1L, requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());
        verify(duenoRepository, times(1)).save(any(Dueno.class));
    }

    @Test
    void actualizarDueno_cuandoNoExiste_debeLanzarExcepcion() {
        // Given
        when(duenoRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> duenoService.actualizarDueno(99L, requestDTO));
        verify(duenoRepository, never()).save(any());
    }

    // ─── eliminarDueno ────────────────────────────────────────────────────────

    @Test
    void eliminarDueno_cuandoExiste_debeEliminarCorrectamente() {
        // Given
        when(duenoRepository.findById(1L)).thenReturn(Optional.of(dueno));
        doNothing().when(duenoRepository).delete(dueno);

        // When
        duenoService.eliminarDueno(1L);

        // Then
        verify(duenoRepository, times(1)).delete(dueno);
    }

    @Test
    void eliminarDueno_cuandoNoExiste_debeLanzarExcepcion() {
        // Given
        when(duenoRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> duenoService.eliminarDueno(99L));
        verify(duenoRepository, never()).delete(any());
    }

    // ─── comunicación con MascotaClient ───────────────────────────────────────

    @Test
    void obtenerPorId_debeIncluirMascotasDelDueno() {
        // Given
        MascotaDTO mascota = new MascotaDTO();
        when(duenoRepository.findById(1L)).thenReturn(Optional.of(dueno));
        when(mascotaClient.obtenerMascotasPorDueno(1L)).thenReturn(List.of(mascota));

        // When
        DuenoResponseDTO resultado = duenoService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado.getMascotas());
        assertEquals(1, resultado.getMascotas().size());
        verify(mascotaClient, times(1)).obtenerMascotasPorDueno(1L);
    }
}
