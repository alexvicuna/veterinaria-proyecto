package com.veterinaria.historiales_medicos;

import com.veterinaria.historiales_medicos.dto.HistorialRequestDTO;
import com.veterinaria.historiales_medicos.dto.HistorialResponseDTO;
import com.veterinaria.historiales_medicos.model.Historial;
import com.veterinaria.historiales_medicos.repository.HistorialRepository;
import com.veterinaria.historiales_medicos.service.HistorialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialServiceTest {


    @Mock
    private HistorialRepository historialRepository;

    @InjectMocks
    private HistorialService historialService;


    private Historial historialGuardado;
    private HistorialRequestDTO requestValido;

    @BeforeEach
    void setUp() {
        historialGuardado = new Historial();
        historialGuardado.setIdHistorial(1L);
        historialGuardado.setDiagnostico("Infección gastrointestinal");
        historialGuardado.setTratamiento("Antibióticos por 7 días");
        historialGuardado.setVacunas("Rabia, Parvovirus");
        historialGuardado.setObservaciones("Reposo 3 días");
        historialGuardado.setFechaAtencion(LocalDate.of(2025, 3, 15));
        historialGuardado.setIdMascota(5L);
        historialGuardado.setIdVeterinario(2L);

        requestValido = new HistorialRequestDTO();
        requestValido.setDiagnostico("Infección gastrointestinal");
        requestValido.setTratamiento("Antibióticos por 7 días");
        requestValido.setVacunas("Rabia, Parvovirus");
        requestValido.setObservaciones("Reposo 3 días");
        requestValido.setFechaAtencion(LocalDate.of(2025, 3, 15));
        requestValido.setIdMascota(5L);
        requestValido.setIdVeterinario(2L);
    }


    @Test
    @DisplayName("crearHistorial - debería guardar y retornar el historial correctamente")
    void crearHistorial_exitoso() {
        // GIVEN
        when(historialRepository.save(any(Historial.class))).thenReturn(historialGuardado);

        // WHEN
        HistorialResponseDTO resultado = historialService.crearHistorial(requestValido);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdHistorial());
        assertEquals("Infección gastrointestinal", resultado.getDiagnostico());
        assertEquals("Antibióticos por 7 días", resultado.getTratamiento());
        assertEquals("Rabia, Parvovirus", resultado.getVacunas());
        assertEquals(5L, resultado.getIdMascota());
        assertEquals(2L, resultado.getIdVeterinario());
        verify(historialRepository, times(1)).save(any(Historial.class));
    }

    @Test
    @DisplayName("crearHistorial - debería asignar la fecha de atención correctamente")
    void crearHistorial_asignaFechaAtencion() {
        when(historialRepository.save(any(Historial.class))).thenReturn(historialGuardado);

        HistorialResponseDTO resultado = historialService.crearHistorial(requestValido);

        assertEquals(LocalDate.of(2025, 3, 15), resultado.getFechaAtencion());
    }

    @Test
    @DisplayName("obtenerTodos - debería retornar todos los historiales existentes")
    void obtenerTodos_retornaLista() {
        // GIVEN: dos historiales en BD
        Historial historial2 = new Historial();
        historial2.setIdHistorial(2L);
        historial2.setDiagnostico("Fractura en pata delantera");
        historial2.setTratamiento("Inmovilización con yeso");
        historial2.setFechaAtencion(LocalDate.of(2025, 4, 10));
        historial2.setIdMascota(8L);
        historial2.setIdVeterinario(3L);

        when(historialRepository.findAll()).thenReturn(List.of(historialGuardado, historial2));

        List<HistorialResponseDTO> resultado = historialService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals("Infección gastrointestinal", resultado.get(0).getDiagnostico());
        assertEquals("Fractura en pata delantera", resultado.get(1).getDiagnostico());
    }

    @Test
    @DisplayName("obtenerTodos - debería retornar lista vacía cuando no hay historiales")
    void obtenerTodos_listaVacia() {
        when(historialRepository.findAll()).thenReturn(List.of());

        List<HistorialResponseDTO> resultado = historialService.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }


    @Test
    @DisplayName("obtenerPorId - debería retornar el historial cuando el ID existe")
    void obtenerPorId_encontrado() {

        when(historialRepository.findById(1L)).thenReturn(Optional.of(historialGuardado));

        HistorialResponseDTO resultado = historialService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdHistorial());
        assertEquals("Infección gastrointestinal", resultado.getDiagnostico());
    }

    @Test
    @DisplayName("obtenerPorId - debería lanzar excepción cuando el ID no existe")
    void obtenerPorId_noEncontrado_lanzaExcepcion() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> historialService.obtenerPorId(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("obtenerPorMascota - debería retornar historiales de una mascota específica")
    void obtenerPorMascota_retornaHistoriales() {
        when(historialRepository.findByIdMascota(5L)).thenReturn(List.of(historialGuardado));

        List<HistorialResponseDTO> resultado = historialService.obtenerPorMascota(5L);

        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).getIdMascota());
    }

    @Test
    @DisplayName("obtenerPorMascota - debería retornar lista vacía si la mascota no tiene historiales")
    void obtenerPorMascota_sinHistoriales() {
        when(historialRepository.findByIdMascota(99L)).thenReturn(List.of());

        List<HistorialResponseDTO> resultado = historialService.obtenerPorMascota(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorVeterinario - debería retornar historiales del veterinario indicado")
    void obtenerPorVeterinario_retornaHistoriales() {
        when(historialRepository.findByIdVeterinario(2L)).thenReturn(List.of(historialGuardado));

        List<HistorialResponseDTO> resultado = historialService.obtenerPorVeterinario(2L);

        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getIdVeterinario());
    }

    @Test
    @DisplayName("obtenerPorMascotaYVeterinario - debería filtrar por ambos criterios")
    void obtenerPorMascotaYVeterinario_retornaFiltrado() {
        when(historialRepository.findByIdMascotaAndIdVeterinario(5L, 2L))
                .thenReturn(List.of(historialGuardado));

        List<HistorialResponseDTO> resultado =
                historialService.obtenerPorMascotaYVeterinario(5L, 2L);

        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).getIdMascota());
        assertEquals(2L, resultado.get(0).getIdVeterinario());
    }

    @Test
    @DisplayName("obtenerPorMascotaYVeterinario - debería retornar vacío si no hay coincidencias")
    void obtenerPorMascotaYVeterinario_sinCoincidencias() {
        when(historialRepository.findByIdMascotaAndIdVeterinario(1L, 99L))
                .thenReturn(List.of());

        List<HistorialResponseDTO> resultado =
                historialService.obtenerPorMascotaYVeterinario(1L, 99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorRangoDeFechas - debería retornar historiales dentro del rango")
    void obtenerPorRangoDeFechas_retornaHistoriales() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin    = LocalDate.of(2025, 12, 31);
        when(historialRepository.findByFechaAtencionBetween(inicio, fin))
                .thenReturn(List.of(historialGuardado));

        List<HistorialResponseDTO> resultado =
                historialService.obtenerPorRangoDeFechas(inicio, fin);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getFechaAtencion().isAfter(inicio.minusDays(1)));
        assertTrue(resultado.get(0).getFechaAtencion().isBefore(fin.plusDays(1)));
    }

    @Test
    @DisplayName("obtenerPorRangoDeFechas - debería retornar vacío si no hay atenciones en el rango")
    void obtenerPorRangoDeFechas_sinResultados() {
        LocalDate inicio = LocalDate.of(2020, 1, 1);
        LocalDate fin    = LocalDate.of(2020, 12, 31);
        when(historialRepository.findByFechaAtencionBetween(inicio, fin))
                .thenReturn(List.of());

        List<HistorialResponseDTO> resultado =
                historialService.obtenerPorRangoDeFechas(inicio, fin);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("actualizarHistorial - debería modificar los datos y retornar el historial actualizado")
    void actualizarHistorial_exitoso() {
        HistorialRequestDTO requestActualizar = new HistorialRequestDTO();
        requestActualizar.setDiagnostico("Diagnóstico actualizado");
        requestActualizar.setTratamiento("Tratamiento nuevo");
        requestActualizar.setVacunas("Triple felina");
        requestActualizar.setObservaciones("Sin observaciones");
        requestActualizar.setFechaAtencion(LocalDate.of(2025, 5, 20));
        requestActualizar.setIdMascota(5L);
        requestActualizar.setIdVeterinario(2L);

        Historial historialActualizado = new Historial();
        historialActualizado.setIdHistorial(1L);
        historialActualizado.setDiagnostico("Diagnóstico actualizado");
        historialActualizado.setTratamiento("Tratamiento nuevo");
        historialActualizado.setFechaAtencion(LocalDate.of(2025, 5, 20));
        historialActualizado.setIdMascota(5L);
        historialActualizado.setIdVeterinario(2L);

        when(historialRepository.findById(1L)).thenReturn(Optional.of(historialGuardado));
        when(historialRepository.save(any(Historial.class))).thenReturn(historialActualizado);

        HistorialResponseDTO resultado = historialService.actualizarHistorial(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Diagnóstico actualizado", resultado.getDiagnostico());
        assertEquals("Tratamiento nuevo", resultado.getTratamiento());
        verify(historialRepository, times(1)).save(any(Historial.class));
    }

    @Test
    @DisplayName("actualizarHistorial - debería lanzar excepción si el historial no existe")
    void actualizarHistorial_noEncontrado_lanzaExcepcion() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> historialService.actualizarHistorial(99L, requestValido));

        assertTrue(ex.getMessage().contains("99"));
        verify(historialRepository, never()).save(any(Historial.class));
    }

    @Test
    @DisplayName("eliminarHistorial - debería eliminar el historial existente")
    void eliminarHistorial_exitoso() {
        when(historialRepository.findById(1L)).thenReturn(Optional.of(historialGuardado));

        historialService.eliminarHistorial(1L);

        verify(historialRepository, times(1)).findById(1L);
        verify(historialRepository, times(1)).delete(historialGuardado);
    }

    @Test
    @DisplayName("eliminarHistorial - debería lanzar excepción y NO eliminar si no existe")
    void eliminarHistorial_noEncontrado_noElimina() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> historialService.eliminarHistorial(99L));
        verify(historialRepository, never()).delete(any(Historial.class));
    }
}
