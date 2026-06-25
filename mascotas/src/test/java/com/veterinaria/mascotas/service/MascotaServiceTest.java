package com.veterinaria.mascotas.service;
import com.veterinaria.mascotas.client.DuenoClient;
import com.veterinaria.mascotas.dto.DuenoDTO;
import com.veterinaria.mascotas.dto.MascotaDTO;
import com.veterinaria.mascotas.dto.MascotaRequestDTO;
import com.veterinaria.mascotas.dto.MascotaResponseDTO;
import com.veterinaria.mascotas.mascotaException.MascotaNotFoundException;
import com.veterinaria.mascotas.model.Mascota;
import com.veterinaria.mascotas.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - MascotaService")
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private DuenoClient duenoClient;

    @InjectMocks
    private MascotaService mascotaService;

    private Mascota mascotaGuardada;
    private MascotaRequestDTO requestDTO;
    private DuenoDTO duenoDTO;

    @BeforeEach
    void setUp() {
        duenoDTO = new DuenoDTO();
        duenoDTO.setIdDueno(1L);
        duenoDTO.setNombre("Carlos");
        duenoDTO.setApellido("Pérez");
        duenoDTO.setRut("12345678-9");
        duenoDTO.setTelefono("912345678");
        duenoDTO.setCorreo("carlos@mail.com");

        mascotaGuardada = new Mascota();
        mascotaGuardada.setIdMascota(1L);
        mascotaGuardada.setNombreMasc("Firulais");
        mascotaGuardada.setEspecie("Perro");
        mascotaGuardada.setRaza("Labrador");
        mascotaGuardada.setEdad(3);
        mascotaGuardada.setIdDueno(1L);


        requestDTO = new MascotaRequestDTO();
        requestDTO.setNombreMasc("Firulais");
        requestDTO.setEspecie("Perro");
        requestDTO.setRaza("Labrador");
        requestDTO.setEdad(3);
        requestDTO.setIdDueno(1L);
    }


    @Test
    @DisplayName("crearMascota - debe guardar y retornar la mascota con datos del dueño")
    void crearMascota_cuandoDatosValidos_debeRetornarResponseDTO() {

        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaGuardada);
        when(duenoClient.obtenerDuenoPorId(1L)).thenReturn(duenoDTO);


        MascotaResponseDTO resultado = mascotaService.crearMascota(requestDTO);


        assertNotNull(resultado);
        assertEquals("Firulais", resultado.getNombreMasc());
        assertEquals("Perro", resultado.getEspecie());
        assertEquals("Labrador", resultado.getRaza());
        assertEquals(3, resultado.getEdad());
        assertNotNull(resultado.getDueno());
        assertEquals("Carlos", resultado.getDueno().getNombre());

        verify(mascotaRepository, times(1)).save(any(Mascota.class));
        verify(duenoClient, times(1)).obtenerDuenoPorId(1L);
    }

    @Test
    @DisplayName("crearMascota - debe mapear correctamente todos los campos del request al modelo")
    void crearMascota_debeMapearCamposCorrectamente() {
        // Given
        requestDTO.setNombreMasc("Luna");
        requestDTO.setEspecie("Gato");
        requestDTO.setRaza("Siamés");
        requestDTO.setEdad(2);
        requestDTO.setIdDueno(1L);

        Mascota mascotaGuardadaGato = new Mascota();
        mascotaGuardadaGato.setIdMascota(2L);
        mascotaGuardadaGato.setNombreMasc("Luna");
        mascotaGuardadaGato.setEspecie("Gato");
        mascotaGuardadaGato.setRaza("Siamés");
        mascotaGuardadaGato.setEdad(2);
        mascotaGuardadaGato.setIdDueno(1L);

        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaGuardadaGato);
        when(duenoClient.obtenerDuenoPorId(1L)).thenReturn(duenoDTO);


        MascotaResponseDTO resultado = mascotaService.crearMascota(requestDTO);


        assertEquals(2L, resultado.getIdMascota());
        assertEquals("Luna", resultado.getNombreMasc());
        assertEquals("Gato", resultado.getEspecie());
        assertEquals("Siamés", resultado.getRaza());
        assertEquals(2, resultado.getEdad());
    }


    @Test
    @DisplayName("obtenerTodas - debe retornar lista con todas las mascotas")
    void obtenerTodas_cuandoExistenMascotas_debeRetornarLista() {
        // Given
        Mascota otraMascota = new Mascota();
        otraMascota.setIdMascota(2L);
        otraMascota.setNombreMasc("Max");
        otraMascota.setEspecie("Perro");
        otraMascota.setRaza("Beagle");
        otraMascota.setEdad(5);
        otraMascota.setIdDueno(1L);

        when(mascotaRepository.findAll()).thenReturn(List.of(mascotaGuardada, otraMascota));
        when(duenoClient.obtenerDuenoPorId(anyLong())).thenReturn(duenoDTO);


        List<MascotaResponseDTO> resultado = mascotaService.obtenerTodas();


        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Firulais", resultado.get(0).getNombreMasc());
        assertEquals("Max", resultado.get(1).getNombreMasc());

        verify(mascotaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas - debe retornar lista vacía cuando no hay mascotas")
    void obtenerTodas_cuandoNoExistenMascotas_debeRetornarListaVacia() {

        when(mascotaRepository.findAll()).thenReturn(Collections.emptyList());


        List<MascotaResponseDTO> resultado = mascotaService.obtenerTodas();


        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(mascotaRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("obtenerPorId - debe retornar la mascota cuando existe el ID")
    void obtenerPorId_cuandoExisteId_debeRetornarMascota() {

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaGuardada));
        when(duenoClient.obtenerDuenoPorId(1L)).thenReturn(duenoDTO);


        MascotaResponseDTO resultado = mascotaService.obtenerPorId(1L);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdMascota());
        assertEquals("Firulais", resultado.getNombreMasc());
        verify(mascotaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - debe lanzar MascotaNotFoundException cuando el ID no existe")
    void obtenerPorId_cuandoNoExisteId_debeLanzarExcepcion() {

        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());


        MascotaNotFoundException excepcion = assertThrows(
                MascotaNotFoundException.class,
                () -> mascotaService.obtenerPorId(99L)
        );

        assertTrue(excepcion.getMessage().contains("99"));
        verify(mascotaRepository, times(1)).findById(99L);
        verify(duenoClient, never()).obtenerDuenoPorId(anyLong());
    }



    @Test
    @DisplayName("actualizar - debe modificar los datos cuando existe la mascota")
    void actualizar_cuandoExisteMascota_debeActualizarYRetornar() {

        MascotaRequestDTO requestActualizar = new MascotaRequestDTO();
        requestActualizar.setNombreMasc("Firulais Actualizado");
        requestActualizar.setEspecie("Perro");
        requestActualizar.setRaza("Golden Retriever");
        requestActualizar.setEdad(4);
        requestActualizar.setIdDueno(1L);

        Mascota mascotaActualizada = new Mascota();
        mascotaActualizada.setIdMascota(1L);
        mascotaActualizada.setNombreMasc("Firulais Actualizado");
        mascotaActualizada.setEspecie("Perro");
        mascotaActualizada.setRaza("Golden Retriever");
        mascotaActualizada.setEdad(4);
        mascotaActualizada.setIdDueno(1L);

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaGuardada));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaActualizada);
        when(duenoClient.obtenerDuenoPorId(1L)).thenReturn(duenoDTO);


        MascotaResponseDTO resultado = mascotaService.actualizar(1L, requestActualizar);


        assertNotNull(resultado);
        assertEquals("Firulais Actualizado", resultado.getNombreMasc());
        assertEquals("Golden Retriever", resultado.getRaza());
        assertEquals(4, resultado.getEdad());

        verify(mascotaRepository, times(1)).findById(1L);
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    @Test
    @DisplayName("actualizar - debe lanzar excepción cuando la mascota no existe")
    void actualizar_cuandoNoExisteMascota_debeLanzarExcepcion() {

        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                MascotaNotFoundException.class,
                () -> mascotaService.actualizar(99L, requestDTO)
        );

        verify(mascotaRepository, times(1)).findById(99L);
        verify(mascotaRepository, never()).save(any(Mascota.class));
    }


    @Test
    @DisplayName("eliminar - debe eliminar la mascota cuando existe el ID")
    void eliminar_cuandoExisteId_debeEliminar() {

        when(mascotaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(mascotaRepository).deleteById(1L);


        mascotaService.eliminar(1L);


        verify(mascotaRepository, times(1)).existsById(1L);
        verify(mascotaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - debe lanzar excepción cuando el ID no existe")
    void eliminar_cuandoNoExisteId_debeLanzarExcepcion() {

        when(mascotaRepository.existsById(99L)).thenReturn(false);


        MascotaNotFoundException excepcion = assertThrows(
                MascotaNotFoundException.class,
                () -> mascotaService.eliminar(99L)
        );

        assertTrue(excepcion.getMessage().contains("99"));
        verify(mascotaRepository, times(1)).existsById(99L);
        verify(mascotaRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("buscarPorNombre - debe retornar mascotas que coincidan con el nombre")
    void buscarPorNombre_cuandoExistenCoincidencias_debeRetornarLista() {

        when(mascotaRepository.findByNombreMascContainingIgnoreCase("firu"))
                .thenReturn(List.of(mascotaGuardada));
        when(duenoClient.obtenerDuenoPorId(1L)).thenReturn(duenoDTO);

        List<MascotaResponseDTO> resultado = mascotaService.buscarPorNombre("firu");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Firulais", resultado.get(0).getNombreMasc());
    }

    @Test
    @DisplayName("buscarPorNombre - debe retornar lista vacía si no hay coincidencias")
    void buscarPorNombre_cuandoNoHayCoincidencias_debeRetornarListaVacia() {

        when(mascotaRepository.findByNombreMascContainingIgnoreCase("xyz"))
                .thenReturn(Collections.emptyList());


        List<MascotaResponseDTO> resultado = mascotaService.buscarPorNombre("xyz");


        assertTrue(resultado.isEmpty());
        verify(duenoClient, never()).obtenerDuenoPorId(anyLong());
    }

    @Test
    @DisplayName("buscarPorRaza - debe retornar mascotas de la raza indicada")
    void buscarPorRaza_cuandoExistenMascotas_debeRetornarLista() {

        when(mascotaRepository.findByRazaContainingIgnoreCase("Labrador"))
                .thenReturn(List.of(mascotaGuardada));
        when(duenoClient.obtenerDuenoPorId(1L)).thenReturn(duenoDTO);


        List<MascotaResponseDTO> resultado = mascotaService.buscarPorRaza("Labrador");


        assertEquals(1, resultado.size());
        assertEquals("Labrador", resultado.get(0).getRaza());
    }

    @Test
    @DisplayName("buscarPorRaza - debe retornar vacío si no hay mascotas de esa raza")
    void buscarPorRaza_cuandoNoHayCoincidencias_debeRetornarListaVacia() {

        when(mascotaRepository.findByRazaContainingIgnoreCase("Pitbull"))
                .thenReturn(Collections.emptyList());


        List<MascotaResponseDTO> resultado = mascotaService.buscarPorRaza("Pitbull");


        assertTrue(resultado.isEmpty());
    }


    @Test
    @DisplayName("buscarPorEspecie - debe retornar mascotas de la especie indicada")
    void buscarPorEspecie_cuandoExistenMascotas_debeRetornarLista() {

        when(mascotaRepository.findByEspecieContainingIgnoreCase("Perro"))
                .thenReturn(List.of(mascotaGuardada));
        when(duenoClient.obtenerDuenoPorId(1L)).thenReturn(duenoDTO);


        List<MascotaResponseDTO> resultado = mascotaService.buscarPorEspecie("Perro");


        assertEquals(1, resultado.size());
        assertEquals("Perro", resultado.get(0).getEspecie());
    }

    @Test
    @DisplayName("buscarPorEspecie - debe retornar vacío si no hay mascotas de esa especie")
    void buscarPorEspecie_cuandoNoHayCoincidencias_debeRetornarListaVacia() {

        when(mascotaRepository.findByEspecieContainingIgnoreCase("Reptil"))
                .thenReturn(Collections.emptyList());


        List<MascotaResponseDTO> resultado = mascotaService.buscarPorEspecie("Reptil");


        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscarPorDueno - debe retornar lista de mascotas del dueño indicado")
    void buscarPorDueno_cuandoExistenMascotas_debeRetornarListaSimple() {

        when(mascotaRepository.findByIdDueno(1L)).thenReturn(List.of(mascotaGuardada));


        List<MascotaDTO> resultado = mascotaService.buscarPorDueno(1L);


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Firulais", resultado.get(0).getNombreMasc());
        assertEquals("Perro", resultado.get(0).getEspecie());

        verify(duenoClient, never()).obtenerDuenoPorId(anyLong());
    }

    @Test
    @DisplayName("buscarPorDueno - debe retornar lista vacía si el dueño no tiene mascotas")
    void buscarPorDueno_cuandoNoTieneMascotas_debeRetornarListaVacia() {

        when(mascotaRepository.findByIdDueno(99L)).thenReturn(Collections.emptyList());


        List<MascotaDTO> resultado = mascotaService.buscarPorDueno(99L);


        assertTrue(resultado.isEmpty());
        verify(duenoClient, never()).obtenerDuenoPorId(anyLong());
    }

    @Test
    @DisplayName("buscarPorDueno - el DTO simple no debe incluir el dueño (evita llamada circular)")
    void buscarPorDueno_elDTONoDebeIncluirDueno() {

        when(mascotaRepository.findByIdDueno(1L)).thenReturn(List.of(mascotaGuardada));


        List<MascotaDTO> resultado = mascotaService.buscarPorDueno(1L);

        assertNotNull(resultado.get(0));
        assertEquals(1L, resultado.get(0).getIdMascota());
        assertEquals("Firulais", resultado.get(0).getNombreMasc());
        verify(duenoClient, never()).obtenerDuenoPorId(anyLong());
    }
}