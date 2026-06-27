package com.veterinaria.recetas.service;

import com.veterinaria.recetas.client.CitaClient;
import com.veterinaria.recetas.client.MascotaClient;
import com.veterinaria.recetas.client.VeterinarioClient;
import com.veterinaria.recetas.dto.*;
import com.veterinaria.recetas.model.Receta;
import com.veterinaria.recetas.recetasException.RecetaNotFoundException;
import com.veterinaria.recetas.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - RecetaService")
class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private MascotaClient mascotaClient;

    @Mock
    private VeterinarioClient veterinarioClient;

    @Mock
    private CitaClient citaClient;

    @InjectMocks
    private RecetaService recetaService;

    // ──────────────────────────────────────────────
    // Datos de prueba reutilizables
    // ──────────────────────────────────────────────

    private Receta recetaGuardada;
    private RecetaRequestDTO requestDTO;
    private MascotaDTO mascotaDTO;
    private VeterinarioDTO veterinarioDTO;
    private CitaDTO citaDTO;

    @BeforeEach
    void setUp() {
        mascotaDTO = new MascotaDTO();
        mascotaDTO.setIdMascota(1L);
        mascotaDTO.setNombreMasc("Firulais");
        mascotaDTO.setEspecie("Perro");
        mascotaDTO.setRaza("Labrador");

        veterinarioDTO = new VeterinarioDTO();
        veterinarioDTO.setIdVeterinario(1L);
        veterinarioDTO.setNombreVet("Ana");
        veterinarioDTO.setApellidoVet("González");
        veterinarioDTO.setEspecialidad("Cirugía");

        citaDTO = new CitaDTO();
        citaDTO.setIdCita(1L);
        citaDTO.setMotivoConsulta("Control general");
        citaDTO.setEstadoCita("CONFIRMADA");

        recetaGuardada = new Receta();
        recetaGuardada.setIdReceta(1L);
        recetaGuardada.setFechaEmision(LocalDate.now());
        recetaGuardada.setDiagnostico("Infección bacteriana");
        recetaGuardada.setMedicamento("Amoxicilina");
        recetaGuardada.setDosis("500mg cada 8 horas por 7 días");
        recetaGuardada.setIdVeterinario(1L);
        recetaGuardada.setIdMascota(1L);
        recetaGuardada.setIdCita(1L);

        requestDTO = new RecetaRequestDTO();
        requestDTO.setFechaEmision(LocalDate.now());
        requestDTO.setDiagnostico("Infección bacteriana");
        requestDTO.setMedicamento("Amoxicilina");
        requestDTO.setDosis("500mg cada 8 horas por 7 días");
        requestDTO.setIdVeterinario(1L);
        requestDTO.setIdMascota(1L);
        requestDTO.setIdCita(1L);
    }

    // ══════════════════════════════════════════════
    // crearReceta
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("crearReceta - debe guardar y retornar la receta con datos de mascota, veterinario y cita")
    void crearReceta_cuandoDatosValidos_debeRetornarResponseDTO() {
        // Given
        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaGuardada);
        when(mascotaClient.obtenerMascota(1L)).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(1L)).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // When
        RecetaResponseDTO resultado = recetaService.crearReceta(requestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdReceta());
        assertEquals("Infección bacteriana", resultado.getDiagnostico());
        assertEquals("Amoxicilina", resultado.getMedicamento());
        assertEquals("500mg cada 8 horas por 7 días", resultado.getDosis());
        assertNotNull(resultado.getMascota());
        assertNotNull(resultado.getVeterinario());
        assertNotNull(resultado.getCita());

        verify(recetaRepository, times(1)).save(any(Receta.class));
        verify(mascotaClient, times(1)).obtenerMascota(1L);
        verify(veterinarioClient, times(1)).obtenerVeterinario(1L);
        verify(citaClient, times(1)).obtenerCita(1L);
    }

    @Test
    @DisplayName("crearReceta - debe mapear correctamente todos los campos del request")
    void crearReceta_debeMapearCamposCorrectamente() {
        // Given
        requestDTO.setMedicamento("Ibuprofeno");
        requestDTO.setDosis("200mg cada 12 horas");

        Receta recetaIbuprofeno = new Receta();
        recetaIbuprofeno.setIdReceta(2L);
        recetaIbuprofeno.setFechaEmision(LocalDate.now());
        recetaIbuprofeno.setDiagnostico("Infección bacteriana");
        recetaIbuprofeno.setMedicamento("Ibuprofeno");
        recetaIbuprofeno.setDosis("200mg cada 12 horas");
        recetaIbuprofeno.setIdVeterinario(1L);
        recetaIbuprofeno.setIdMascota(1L);
        recetaIbuprofeno.setIdCita(1L);

        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaIbuprofeno);
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(anyLong())).thenReturn(citaDTO);

        // When
        RecetaResponseDTO resultado = recetaService.crearReceta(requestDTO);

        // Then
        assertEquals("Ibuprofeno", resultado.getMedicamento());
        assertEquals("200mg cada 12 horas", resultado.getDosis());
    }

    // ══════════════════════════════════════════════
    // obtenerTodos
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("obtenerTodos - debe retornar lista con todas las recetas")
    void obtenerTodos_cuandoExistenRecetas_debeRetornarLista() {
        // Given
        Receta otraReceta = new Receta();
        otraReceta.setIdReceta(2L);
        otraReceta.setFechaEmision(LocalDate.now());
        otraReceta.setDiagnostico("Dolor articular");
        otraReceta.setMedicamento("Ibuprofeno");
        otraReceta.setDosis("200mg cada 12 horas");
        otraReceta.setIdVeterinario(1L);
        otraReceta.setIdMascota(1L);
        otraReceta.setIdCita(2L);

        when(recetaRepository.findAll()).thenReturn(List.of(recetaGuardada, otraReceta));
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(anyLong())).thenReturn(citaDTO);

        // When
        List<RecetaResponseDTO> resultado = recetaService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(recetaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos - debe retornar lista vacía cuando no hay recetas")
    void obtenerTodos_cuandoNoExistenRecetas_debeRetornarListaVacia() {
        // Given
        when(recetaRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<RecetaResponseDTO> resultado = recetaService.obtenerTodos();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // ══════════════════════════════════════════════
    // obtenerPorId
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPorId - debe retornar la receta cuando existe el ID")
    void obtenerPorId_cuandoExisteId_debeRetornarReceta() {
        // Given
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaGuardada));
        when(mascotaClient.obtenerMascota(1L)).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(1L)).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // When
        RecetaResponseDTO resultado = recetaService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdReceta());
        assertEquals("Amoxicilina", resultado.getMedicamento());
    }

    @Test
    @DisplayName("obtenerPorId - debe lanzar RecetaNotFoundException cuando el ID no existe")
    void obtenerPorId_cuandoNoExisteId_debeLanzarExcepcion() {
        // Given
        when(recetaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RecetaNotFoundException excepcion = assertThrows(
                RecetaNotFoundException.class,
                () -> recetaService.obtenerPorId(99L)
        );

        assertTrue(excepcion.getMessage().contains("99"));
        verify(recetaRepository, times(1)).findById(99L);
    }

    // ══════════════════════════════════════════════
    // obtenerPorMascota
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPorMascota - debe retornar recetas de la mascota indicada")
    void obtenerPorMascota_cuandoExistenRecetas_debeRetornarLista() {
        // Given
        when(recetaRepository.findByIdMascota(1L)).thenReturn(List.of(recetaGuardada));
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(anyLong())).thenReturn(citaDTO);

        // When
        List<RecetaResponseDTO> resultado = recetaService.obtenerPorMascota(1L);

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(recetaRepository, times(1)).findByIdMascota(1L);
    }

    @Test
    @DisplayName("obtenerPorMascota - debe retornar vacío si la mascota no tiene recetas")
    void obtenerPorMascota_cuandoNoHayRecetas_debeRetornarListaVacia() {
        // Given
        when(recetaRepository.findByIdMascota(99L)).thenReturn(Collections.emptyList());

        // When
        List<RecetaResponseDTO> resultado = recetaService.obtenerPorMascota(99L);

        // Then
        assertTrue(resultado.isEmpty());
    }

    // ══════════════════════════════════════════════
    // obtenerPorVeterinario
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPorVeterinario - debe retornar recetas del veterinario indicado")
    void obtenerPorVeterinario_cuandoExistenRecetas_debeRetornarLista() {
        // Given
        when(recetaRepository.findByIdVeterinario(1L)).thenReturn(List.of(recetaGuardada));
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(anyLong())).thenReturn(citaDTO);

        // When
        List<RecetaResponseDTO> resultado = recetaService.obtenerPorVeterinario(1L);

        // Then
        assertEquals(1, resultado.size());
        verify(recetaRepository, times(1)).findByIdVeterinario(1L);
    }

    @Test
    @DisplayName("obtenerPorVeterinario - debe retornar vacío si el veterinario no tiene recetas")
    void obtenerPorVeterinario_cuandoNoHayRecetas_debeRetornarListaVacia() {
        // Given
        when(recetaRepository.findByIdVeterinario(99L)).thenReturn(Collections.emptyList());

        // When
        assertTrue(recetaService.obtenerPorVeterinario(99L).isEmpty());
    }

    // ══════════════════════════════════════════════
    // obtenerPorMascotaYVeterinario
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPorMascotaYVeterinario - debe retornar recetas que coincidan con ambos filtros")
    void obtenerPorMascotaYVeterinario_cuandoExistenRecetas_debeRetornarLista() {
        // Given
        when(recetaRepository.findByIdMascotaAndIdVeterinario(1L, 1L))
                .thenReturn(List.of(recetaGuardada));
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(anyLong())).thenReturn(citaDTO);

        // When
        List<RecetaResponseDTO> resultado = recetaService.obtenerPorMascotaYVeterinario(1L, 1L);

        // Then
        assertEquals(1, resultado.size());
        verify(recetaRepository, times(1)).findByIdMascotaAndIdVeterinario(1L, 1L);
    }

    @Test
    @DisplayName("obtenerPorMascotaYVeterinario - debe retornar vacío si no hay coincidencias")
    void obtenerPorMascotaYVeterinario_cuandoNoHayCoincidencias_debeRetornarListaVacia() {
        // Given
        when(recetaRepository.findByIdMascotaAndIdVeterinario(99L, 99L))
                .thenReturn(Collections.emptyList());

        // When
        assertTrue(recetaService.obtenerPorMascotaYVeterinario(99L, 99L).isEmpty());
    }

    // ══════════════════════════════════════════════
    // obtenerPorRangoDeFechas
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPorRangoDeFechas - debe retornar recetas dentro del rango indicado")
    void obtenerPorRangoDeFechas_cuandoExistenRecetas_debeRetornarLista() {
        // Given
        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();

        when(recetaRepository.findByFechaEmisionBetween(inicio, fin))
                .thenReturn(List.of(recetaGuardada));
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(anyLong())).thenReturn(citaDTO);

        // When
        List<RecetaResponseDTO> resultado = recetaService.obtenerPorRangoDeFechas(inicio, fin);

        // Then
        assertEquals(1, resultado.size());
        verify(recetaRepository, times(1)).findByFechaEmisionBetween(inicio, fin);
    }

    @Test
    @DisplayName("obtenerPorRangoDeFechas - debe retornar vacío si no hay recetas en el rango")
    void obtenerPorRangoDeFechas_cuandoNoHayRecetas_debeRetornarListaVacia() {
        // Given
        LocalDate inicio = LocalDate.of(2020, 1, 1);
        LocalDate fin = LocalDate.of(2020, 1, 31);
        when(recetaRepository.findByFechaEmisionBetween(inicio, fin))
                .thenReturn(Collections.emptyList());

        // When
        assertTrue(recetaService.obtenerPorRangoDeFechas(inicio, fin).isEmpty());
    }

    // ══════════════════════════════════════════════
    // actualizarReceta
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("actualizarReceta - debe modificar los datos cuando existe la receta")
    void actualizarReceta_cuandoExisteReceta_debeActualizarYRetornar() {
        // Given
        RecetaRequestDTO requestActualizar = new RecetaRequestDTO();
        requestActualizar.setFechaEmision(LocalDate.now());
        requestActualizar.setDiagnostico("Dolor articular");
        requestActualizar.setMedicamento("Ibuprofeno");
        requestActualizar.setDosis("200mg cada 12 horas");
        requestActualizar.setIdVeterinario(1L);
        requestActualizar.setIdMascota(1L);
        requestActualizar.setIdCita(1L);

        Receta recetaActualizada = new Receta();
        recetaActualizada.setIdReceta(1L);
        recetaActualizada.setFechaEmision(LocalDate.now());
        recetaActualizada.setDiagnostico("Dolor articular");
        recetaActualizada.setMedicamento("Ibuprofeno");
        recetaActualizada.setDosis("200mg cada 12 horas");
        recetaActualizada.setIdVeterinario(1L);
        recetaActualizada.setIdMascota(1L);
        recetaActualizada.setIdCita(1L);

        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaGuardada));
        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaActualizada);
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);
        when(citaClient.obtenerCita(anyLong())).thenReturn(citaDTO);

        // When
        RecetaResponseDTO resultado = recetaService.actualizarReceta(1L, requestActualizar);

        // Then
        assertEquals("Ibuprofeno", resultado.getMedicamento());
        assertEquals("Dolor articular", resultado.getDiagnostico());
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    @DisplayName("actualizarReceta - debe lanzar excepción cuando la receta no existe")
    void actualizarReceta_cuandoNoExisteReceta_debeLanzarExcepcion() {
        // Given
        when(recetaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RecetaNotFoundException.class,
                () -> recetaService.actualizarReceta(99L, requestDTO));
        verify(recetaRepository, never()).save(any(Receta.class));
    }

    // ══════════════════════════════════════════════
    // eliminarReceta
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("eliminarReceta - debe eliminar la receta cuando existe")
    void eliminarReceta_cuandoExisteId_debeEliminar() {
        // Given
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaGuardada));
        doNothing().when(recetaRepository).delete(any(Receta.class));

        // When
        recetaService.eliminarReceta(1L);

        // Then
        verify(recetaRepository, times(1)).findById(1L);
        verify(recetaRepository, times(1)).delete(recetaGuardada);
    }

    @Test
    @DisplayName("eliminarReceta - debe lanzar excepción cuando el ID no existe")
    void eliminarReceta_cuandoNoExisteId_debeLanzarExcepcion() {
        // Given
        when(recetaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RecetaNotFoundException excepcion = assertThrows(
                RecetaNotFoundException.class,
                () -> recetaService.eliminarReceta(99L)
        );

        assertTrue(excepcion.getMessage().contains("99"));
        verify(recetaRepository, never()).delete(any(Receta.class));
    }
}