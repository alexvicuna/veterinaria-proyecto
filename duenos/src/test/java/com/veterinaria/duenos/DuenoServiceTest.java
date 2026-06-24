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


    @Test
    void registrarDueno_cuandoDatosValidos_debeRetornarDuenoGuardado() {
        when(duenoRepository.save(any(Dueno.class))).thenReturn(dueno);
        when(mascotaClient.obtenerMascotasPorDueno(1L)).thenReturn(List.of());

        DuenoResponseDTO resultado = duenoService.registrarDueno(requestDTO);


        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Pérez", resultado.getApellido());
        verify(duenoRepository, times(1)).save(any(Dueno.class));
    }

    @Test
    void registrarDueno_cuandoSeGuarda_debeLlamarAlRepositorio() {

        when(duenoRepository.save(any(Dueno.class))).thenReturn(dueno);
        when(mascotaClient.obtenerMascotasPorDueno(anyLong())).thenReturn(List.of());

        duenoService.registrarDueno(requestDTO);

        verify(duenoRepository, times(1)).save(any(Dueno.class));
    }

    @Test
    void obtenerTodos_cuandoExistenDuenos_debeRetornarLista() {
        // Given
        Dueno dueno2 = new Dueno();
        dueno2.setIdDueno(2L);
        dueno2.setNombre("María");
        when(duenoRepository.findAll()).thenReturn(List.of(dueno, dueno2));
        when(mascotaClient.obtenerMascotasPorDueno(anyLong())).thenReturn(List.of());


        List<DuenoResponseDTO> resultado = duenoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(duenoRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodos_cuandoNoHayDuenos_debeRetornarListaVacia() {
        when(duenoRepository.findAll()).thenReturn(List.of());

        List<DuenoResponseDTO> resultado = duenoService.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }


    @Test
    void obtenerPorId_cuandoExisteElDueno_debeRetornarDueno() {
        when(duenoRepository.findById(1L)).thenReturn(Optional.of(dueno));
        when(mascotaClient.obtenerMascotasPorDueno(1L)).thenReturn(List.of());

        DuenoResponseDTO resultado = duenoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdDueno());
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(duenoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> duenoService.obtenerPorId(99L));
        verify(duenoRepository, times(1)).findById(99L);
    }

    @Test
    void obtenerPorRut_cuandoExisteElRut_debeRetornarDueno() {
        when(duenoRepository.findByRut("12345678-9")).thenReturn(Optional.of(dueno));
        when(mascotaClient.obtenerMascotasPorDueno(1L)).thenReturn(List.of());

        DuenoResponseDTO resultado = duenoService.obtenerPorRut("12345678-9");

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
    }

    @Test
    void obtenerPorRut_cuandoNoExisteElRut_debeLanzarDuenoNotFoundException() {

        when(duenoRepository.findByRut("99999999-9")).thenReturn(Optional.empty());


        assertThrows(DuenoNotFoundException.class, () -> duenoService.obtenerPorRut("99999999-9"));
    }



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

        DuenoResponseDTO resultado = duenoService.actualizarDueno(1L, requestDTO);

        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());
        verify(duenoRepository, times(1)).save(any(Dueno.class));
    }

    @Test
    void actualizarDueno_cuandoNoExiste_debeLanzarExcepcion() {
        when(duenoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> duenoService.actualizarDueno(99L, requestDTO));
        verify(duenoRepository, never()).save(any());
    }


    @Test
    void eliminarDueno_cuandoExiste_debeEliminarCorrectamente() {
        when(duenoRepository.findById(1L)).thenReturn(Optional.of(dueno));
        doNothing().when(duenoRepository).delete(dueno);

        duenoService.eliminarDueno(1L);

        verify(duenoRepository, times(1)).delete(dueno);
    }

    @Test
    void eliminarDueno_cuandoNoExiste_debeLanzarExcepcion() {
        when(duenoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> duenoService.eliminarDueno(99L));
        verify(duenoRepository, never()).delete(any());
    }

    @Test
    void obtenerPorId_debeIncluirMascotasDelDueno() {
        MascotaDTO mascota = new MascotaDTO();
        when(duenoRepository.findById(1L)).thenReturn(Optional.of(dueno));
        when(mascotaClient.obtenerMascotasPorDueno(1L)).thenReturn(List.of(mascota));

        DuenoResponseDTO resultado = duenoService.obtenerPorId(1L);
        
        assertNotNull(resultado.getMascotas());
        assertEquals(1, resultado.getMascotas().size());
        verify(mascotaClient, times(1)).obtenerMascotasPorDueno(1L);
    }
}
