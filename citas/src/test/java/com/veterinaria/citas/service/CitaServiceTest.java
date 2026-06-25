package com.veterinaria.citas.service;

import com.veterinaria.citas.client.DuenoClient;
import com.veterinaria.citas.client.MascotaClient;
import com.veterinaria.citas.client.VeterinarioClient;
import com.veterinaria.citas.dto.*;
import com.veterinaria.citas.model.Cita;
import com.veterinaria.citas.model.EstadoCita;
import com.veterinaria.citas.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - CitaService")
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private MascotaClient mascotaClient;

    @Mock
    private DuenoClient duenoClient;

    @Mock
    private VeterinarioClient veterinarioClient;

    @InjectMocks
    private CitaService citaService;


    private Cita citaGuardada;
    private CitaRequestDTO requestDTO;
    private MascotaDTO mascotaDTO;
    private DuenoDTO duenoDTO;
    private VeterinarioDTO veterinarioDTO;

    @BeforeEach
    void setUp() {
        // Mascota de prueba
        mascotaDTO = new MascotaDTO();
        mascotaDTO.setIdMascota(1L);
        mascotaDTO.setNombreMasc("Firulais");
        mascotaDTO.setEspecie("Perro");
        mascotaDTO.setRaza("Labrador");
        mascotaDTO.setEdad(3);

        // Dueño de prueba
        duenoDTO = new DuenoDTO();
        duenoDTO.setIdDueno(1L);
        duenoDTO.setNombre("Carlos");
        duenoDTO.setApellido("Pérez");
        duenoDTO.setRut("12345678-9");
        duenoDTO.setTelefono("912345678");
        duenoDTO.setCorreo("carlos@mail.com");

        veterinarioDTO = new VeterinarioDTO();
        veterinarioDTO.setIdVeterinario(1L);
        veterinarioDTO.setNombreVet("Ana");
        veterinarioDTO.setApellidoVet("González");
        veterinarioDTO.setEspecialidad("Cirugía");

        citaGuardada = new Cita();
        citaGuardada.setIdCita(1L);
        citaGuardada.setFechaCita(LocalDateTime.now().plusDays(1));
        citaGuardada.setMotivoConsulta("Control general");
        citaGuardada.setEstadoCita(EstadoCita.PENDIENTE);
        citaGuardada.setIdMascota(1L);
        citaGuardada.setIdDueno(1L);
        citaGuardada.setIdVeterinario(1L);


        requestDTO = new CitaRequestDTO();
        requestDTO.setFechaCita(LocalDateTime.now().plusDays(1));
        requestDTO.setMotivoConsulta("Control general");
        requestDTO.setEstadoCita(EstadoCita.PENDIENTE);
        requestDTO.setIdMascota(1L);
        requestDTO.setIdDueno(1L);
        requestDTO.setIdVeterinario(1L);
    }


    @Test
    @DisplayName("registrarCita - debe guardar la cita y asignar estado PENDIENTE automáticamente")
    void registrarCita_cuandoDatosValidos_debeRetornarResponseDTO() {

        when(citaRepository.save(any(Cita.class))).thenReturn(citaGuardada);
        when(mascotaClient.obtenerMascota(1L)).thenReturn(mascotaDTO);
        when(duenoClient.obtenerDueno(1L)).thenReturn(duenoDTO);
        when(veterinarioClient.obtenerVeterinario(1L)).thenReturn(veterinarioDTO);


        CitaResponseDTO resultado = citaService.registrarCita(requestDTO);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCita());
        assertEquals("Control general", resultado.getMotivoConsulta());
        assertEquals(EstadoCita.PENDIENTE, resultado.getEstadoCita());
        assertNotNull(resultado.getMascota());
        assertNotNull(resultado.getDueno());
        assertNotNull(resultado.getVeterinario());

        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    @DisplayName("registrarCita - debe asignar PENDIENTE sin importar el estado enviado en el request")
    void registrarCita_siempreAsignaPendiente_ignoraEstadoDelRequest() {

        requestDTO.setEstadoCita(EstadoCita.CONFIRMADA);
        when(citaRepository.save(any(Cita.class))).thenReturn(citaGuardada);
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(duenoClient.obtenerDueno(anyLong())).thenReturn(duenoDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);


        CitaResponseDTO resultado = citaService.registrarCita(requestDTO);


        assertEquals(EstadoCita.PENDIENTE, resultado.getEstadoCita());
    }

    @Test
    @DisplayName("obtenerTodas - debe retornar lista con todas las citas")
    void obtenerTodas_cuandoExistenCitas_debeRetornarLista() {
        // Given
        Cita otraCita = new Cita();
        otraCita.setIdCita(2L);
        otraCita.setFechaCita(LocalDateTime.now().plusDays(2));
        otraCita.setMotivoConsulta("Vacuna");
        otraCita.setEstadoCita(EstadoCita.CONFIRMADA);
        otraCita.setIdMascota(1L);
        otraCita.setIdDueno(1L);
        otraCita.setIdVeterinario(1L);

        when(citaRepository.findAll()).thenReturn(List.of(citaGuardada, otraCita));
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(duenoClient.obtenerDueno(anyLong())).thenReturn(duenoDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);

        // When
        List<CitaResponseDTO> resultado = citaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas - debe retornar lista vacía cuando no hay citas")
    void obtenerTodas_cuandoNoExistenCitas_debeRetornarListaVacia() {
        // Given
        when(citaRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CitaResponseDTO> resultado = citaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - debe retornar la cita cuando existe el ID")
    void obtenerPorId_cuandoExisteId_debeRetornarCita() {
        // Given
        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaGuardada));
        when(mascotaClient.obtenerMascota(1L)).thenReturn(mascotaDTO);
        when(duenoClient.obtenerDueno(1L)).thenReturn(duenoDTO);
        when(veterinarioClient.obtenerVeterinario(1L)).thenReturn(veterinarioDTO);

        // When
        CitaResponseDTO resultado = citaService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCita());
        assertEquals("Control general", resultado.getMotivoConsulta());
        verify(citaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - debe lanzar RuntimeException cuando el ID no existe")
    void obtenerPorId_cuandoNoExisteId_debeLanzarExcepcion() {
        // Given
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> citaService.obtenerPorId(99L)
        );

        assertTrue(excepcion.getMessage().contains("99"));
        verify(citaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("actualizarCita - debe modificar los datos cuando existe la cita")
    void actualizarCita_cuandoExisteCita_debeActualizarYRetornar() {
        // Given
        CitaRequestDTO requestActualizar = new CitaRequestDTO();
        requestActualizar.setFechaCita(LocalDateTime.now().plusDays(5));
        requestActualizar.setMotivoConsulta("Vacuna anual");
        requestActualizar.setIdMascota(1L);
        requestActualizar.setIdDueno(1L);
        requestActualizar.setIdVeterinario(1L);

        Cita citaActualizada = new Cita();
        citaActualizada.setIdCita(1L);
        citaActualizada.setFechaCita(requestActualizar.getFechaCita());
        citaActualizada.setMotivoConsulta("Vacuna anual");
        citaActualizada.setEstadoCita(EstadoCita.PENDIENTE);
        citaActualizada.setIdMascota(1L);
        citaActualizada.setIdDueno(1L);
        citaActualizada.setIdVeterinario(1L);

        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaGuardada));
        when(citaRepository.save(any(Cita.class))).thenReturn(citaActualizada);
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(duenoClient.obtenerDueno(anyLong())).thenReturn(duenoDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);

        // When
        CitaResponseDTO resultado = citaService.actualizarCita(1L, requestActualizar);

        // Then
        assertNotNull(resultado);
        assertEquals("Vacuna anual", resultado.getMotivoConsulta());
        verify(citaRepository, times(1)).findById(1L);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    @DisplayName("actualizarCita - debe lanzar excepción cuando la cita no existe")
    void actualizarCita_cuandoNoExisteCita_debeLanzarExcepcion() {
        // Given
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(
                RuntimeException.class,
                () -> citaService.actualizarCita(99L, requestDTO)
        );

        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    @DisplayName("actualizarEstado - debe cambiar el estado de la cita correctamente")
    void actualizarEstado_cuandoExisteCita_debeCambiarEstado() {
        // Given
        ActualizarEstadoDTO estadoDTO = new ActualizarEstadoDTO();
        estadoDTO.setEstadoCita(EstadoCita.CONFIRMADA);

        Cita citaConfirmada = new Cita();
        citaConfirmada.setIdCita(1L);
        citaConfirmada.setFechaCita(citaGuardada.getFechaCita());
        citaConfirmada.setMotivoConsulta("Control general");
        citaConfirmada.setEstadoCita(EstadoCita.CONFIRMADA);
        citaConfirmada.setIdMascota(1L);
        citaConfirmada.setIdDueno(1L);
        citaConfirmada.setIdVeterinario(1L);

        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaGuardada));
        when(citaRepository.save(any(Cita.class))).thenReturn(citaConfirmada);
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(duenoClient.obtenerDueno(anyLong())).thenReturn(duenoDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);

        // When
        CitaResponseDTO resultado = citaService.actualizarEstado(1L, estadoDTO);

        // Then
        assertEquals(EstadoCita.CONFIRMADA, resultado.getEstadoCita());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    @DisplayName("actualizarEstado - debe lanzar excepción cuando la cita no existe")
    void actualizarEstado_cuandoNoExisteCita_debeLanzarExcepcion() {
        // Given
        ActualizarEstadoDTO estadoDTO = new ActualizarEstadoDTO();
        estadoDTO.setEstadoCita(EstadoCita.CANCELADA);
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(
                RuntimeException.class,
                () -> citaService.actualizarEstado(99L, estadoDTO)
        );

        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    @DisplayName("eliminarCita - debe eliminar la cita cuando existe")
    void eliminarCita_cuandoExisteId_debeEliminar() {
        // Given
        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaGuardada));
        doNothing().when(citaRepository).delete(any(Cita.class));

        // When
        citaService.eliminarCita(1L);

        // Then
        verify(citaRepository, times(1)).findById(1L);
        verify(citaRepository, times(1)).delete(citaGuardada);
    }

    @Test
    @DisplayName("eliminarCita - debe lanzar excepción cuando el ID no existe")
    void eliminarCita_cuandoNoExisteId_debeLanzarExcepcion() {
        // Given
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> citaService.eliminarCita(99L)
        );

        assertTrue(excepcion.getMessage().contains("99"));
        verify(citaRepository, never()).delete(any(Cita.class));
    }


    @Test
    @DisplayName("obtenerPorMascota - debe retornar citas de la mascota indicada")
    void obtenerPorMascota_cuandoExistenCitas_debeRetornarLista() {
        // Given
        when(citaRepository.findByIdMascota(1L)).thenReturn(List.of(citaGuardada));
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(duenoClient.obtenerDueno(anyLong())).thenReturn(duenoDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);

        // When
        List<CitaResponseDTO> resultado = citaService.obtenerPorMascota(1L);

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByIdMascota(1L);
    }

    @Test
    @DisplayName("obtenerPorMascota - debe retornar lista vacía si la mascota no tiene citas")
    void obtenerPorMascota_cuandoNoHayCitas_debeRetornarListaVacia() {
        // Given
        when(citaRepository.findByIdMascota(99L)).thenReturn(Collections.emptyList());

        // When
        List<CitaResponseDTO> resultado = citaService.obtenerPorMascota(99L);

        // Then
        assertTrue(resultado.isEmpty());
    }



    @Test
    @DisplayName("obtenerPorFecha - debe retornar citas de la fecha indicada")
    void obtenerPorFecha_cuandoExistenCitas_debeRetornarLista() {
        // Given
        LocalDate fecha = LocalDate.now().plusDays(1);
        when(citaRepository.findByFecha(fecha)).thenReturn(List.of(citaGuardada));
        when(mascotaClient.obtenerMascota(anyLong())).thenReturn(mascotaDTO);
        when(duenoClient.obtenerDueno(anyLong())).thenReturn(duenoDTO);
        when(veterinarioClient.obtenerVeterinario(anyLong())).thenReturn(veterinarioDTO);

        // When
        List<CitaResponseDTO> resultado = citaService.obtenerPorFecha(fecha);

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByFecha(fecha);
    }

    @Test
    @DisplayName("obtenerPorFecha - debe retornar lista vacía si no hay citas en esa fecha")
    void obtenerPorFecha_cuandoNoHayCitas_debeRetornarListaVacia() {
        // Given
        LocalDate fecha = LocalDate.of(2099, 1, 1);
        when(citaRepository.findByFecha(fecha)).thenReturn(Collections.emptyList());

        // When
        List<CitaResponseDTO> resultado = citaService.obtenerPorFecha(fecha);

        // Then
        assertTrue(resultado.isEmpty());
    }
}