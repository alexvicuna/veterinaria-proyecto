package com.veterinaria.boleta;

import com.veterinaria.boleta.boletaException.BoletaNotFoundException;
import com.veterinaria.boleta.dto.*;
import com.veterinaria.boleta.model.Boleta;
import com.veterinaria.boleta.model.DetalleBoleta;
import com.veterinaria.boleta.repository.BoletaRepository;
import com.veterinaria.boleta.service.BoletaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Sin @SpringBootTest: no necesita MySQL ni Spring context
@ExtendWith(MockitoExtension.class)
class BoletaServiceTest {

    @Mock
    private BoletaRepository boletaRepository;

    @InjectMocks
    private BoletaService boletaService;

    private Boleta boletaMock;
    private BoletaRequestDTO requestDTO;
    private DetalleBoletaRequestDTO detalleDTO;

    @BeforeEach
    void setUp() {

        DetalleBoleta detalleMock = new DetalleBoleta();
        detalleMock.setIdDetalle(1L);
        detalleMock.setDescripcion("Consulta general");
        detalleMock.setCantidad(1);
        detalleMock.setSubtotal(25000.0);


        boletaMock = new Boleta();
        boletaMock.setIdBoleta(1L);
        boletaMock.setFecha(LocalDate.of(2025, 5, 10));
        boletaMock.setIdCita(3L);
        boletaMock.setIdPago(5L);
        boletaMock.setTotal(25000.0);
        boletaMock.setDetalles(new ArrayList<>(List.of(detalleMock)));
        detalleMock.setBoleta(boletaMock);


        detalleDTO = new DetalleBoletaRequestDTO();
        detalleDTO.setDescripcion("Consulta general");
        detalleDTO.setCantidad(1);
        detalleDTO.setSubtotal(25000.0);

        requestDTO = new BoletaRequestDTO();
        requestDTO.setFecha(LocalDate.of(2025, 5, 10));
        requestDTO.setIdCita(3L);
        requestDTO.setIdPago(5L);
        requestDTO.setDetalles(List.of(detalleDTO));
    }

    @Test
    void crearBoleta_datosValidos_debeCalcularTotalYGuardar() {
        when(boletaRepository.save(any(Boleta.class))).thenReturn(boletaMock);

        BoletaResponseDTO resultado = boletaService.crearBoleta(requestDTO);

        assertNotNull(resultado);
        assertEquals(25000.0, resultado.getTotal());
        assertEquals(1, resultado.getDetalles().size());
        verify(boletaRepository, times(1)).save(any(Boleta.class));
    }


    @Test
    void crearBoleta_fechaFutura_debeLanzarExcepcion() {

        requestDTO.setFecha(LocalDate.now().plusDays(3));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> boletaService.crearBoleta(requestDTO));

        assertTrue(ex.getMessage().contains("futura"));
        verify(boletaRepository, never()).save(any());
    }


    @Test
    void crearBoleta_sinDetalles_debeLanzarExcepcion() {
        requestDTO.setDetalles(List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> boletaService.crearBoleta(requestDTO));

        assertTrue(ex.getMessage().contains("detalle"));
        verify(boletaRepository, never()).save(any());
    }

    @Test
    void crearBoleta_subtotalCero_debeLanzarExcepcion() {
        detalleDTO.setSubtotal(0.0);
        requestDTO.setDetalles(List.of(detalleDTO));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> boletaService.crearBoleta(requestDTO));

        assertTrue(ex.getMessage().contains("subtotal"));
        verify(boletaRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_idExistente_debeRetornarBoleta() {
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boletaMock));


        BoletaResponseDTO resultado = boletaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdBoleta());
        assertEquals(25000.0, resultado.getTotal());
    }

    @Test
    void obtenerPorId_idInexistente_debeLanzarBoletaNotFoundException() {
        when(boletaRepository.findById(99L)).thenReturn(Optional.empty());

        BoletaNotFoundException ex = assertThrows(BoletaNotFoundException.class,
                () -> boletaService.obtenerPorId(99L));

        assertTrue(ex.getMessage().contains("99"));
    }


    @Test
    void listarTodos_debeRetornarTodasLasBoletas() {

        Boleta segunda = new Boleta();
        segunda.setIdBoleta(2L);
        segunda.setFecha(LocalDate.of(2025, 6, 1));
        segunda.setTotal(50000.0);
        segunda.setDetalles(new ArrayList<>());

        when(boletaRepository.findAll()).thenReturn(List.of(boletaMock, segunda));


        List<BoletaResponseDTO> resultado = boletaService.listarTodos();

        assertEquals(2, resultado.size());
        verify(boletaRepository, times(1)).findAll();
    }

    @Test
    void listarPorFecha_debeRetornarBoletasDeLaFecha() {
        LocalDate fecha = LocalDate.of(2025, 5, 10);
        when(boletaRepository.findByFecha(fecha)).thenReturn(List.of(boletaMock));

        List<BoletaResponseDTO> resultado = boletaService.listarPorFecha(fecha);

        assertEquals(1, resultado.size());
        assertEquals(fecha, resultado.get(0).getFecha());
    }


    @Test
    void listarPorRangoDeFechas_rangoValido_debeRetornarBoletas() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 12, 31);
        when(boletaRepository.findByFechaBetween(inicio, fin)).thenReturn(List.of(boletaMock));

        List<BoletaResponseDTO> resultado = boletaService.listarPorRangoDeFechas(inicio, fin);

        assertEquals(1, resultado.size());
    }


    @Test
    void listarPorRangoDeFechas_inicioMayorQueFin_debeLanzarExcepcion() {
        LocalDate inicio = LocalDate.of(2025, 12, 31);
        LocalDate fin = LocalDate.of(2025, 1, 1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> boletaService.listarPorRangoDeFechas(inicio, fin));

        assertTrue(ex.getMessage().contains("posterior"));
        verify(boletaRepository, never()).findByFechaBetween(any(), any());
    }

    @Test
    void eliminarBoleta_idExistente_debeEliminarSinError() {
        when(boletaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(boletaRepository).deleteById(1L);

        assertDoesNotThrow(() -> boletaService.eliminarBoleta(1L));
        verify(boletaRepository, times(1)).deleteById(1L);
    }


    @Test
    void eliminarBoleta_idInexistente_debeLanzarExcepcion() {
        when(boletaRepository.existsById(50L)).thenReturn(false);

        assertThrows(BoletaNotFoundException.class,
                () -> boletaService.eliminarBoleta(50L));
        verify(boletaRepository, never()).deleteById(any());
    }


    @Test
    void crearBoleta_variosDetalles_debeSumarTotalCorrectamente() {
        DetalleBoletaRequestDTO detalle2 = new DetalleBoletaRequestDTO();
        detalle2.setDescripcion("Vacuna antirrábica");
        detalle2.setCantidad(1);
        detalle2.setSubtotal(20000.0);

        requestDTO.setDetalles(List.of(detalleDTO, detalle2));

        DetalleBoleta d1 = new DetalleBoleta(); d1.setSubtotal(25000.0); d1.setDescripcion("Consulta general"); d1.setCantidad(1);
        DetalleBoleta d2 = new DetalleBoleta(); d2.setSubtotal(20000.0); d2.setDescripcion("Vacuna antirrábica"); d2.setCantidad(1);
        Boleta boletaConTotal = new Boleta();
        boletaConTotal.setIdBoleta(2L);
        boletaConTotal.setFecha(LocalDate.of(2025, 5, 10));
        boletaConTotal.setTotal(45000.0);
        boletaConTotal.setDetalles(List.of(d1, d2));
        d1.setBoleta(boletaConTotal); d2.setBoleta(boletaConTotal);

        when(boletaRepository.save(any(Boleta.class))).thenReturn(boletaConTotal);


        BoletaResponseDTO resultado = boletaService.crearBoleta(requestDTO);

        assertEquals(45000.0, resultado.getTotal());
        assertEquals(2, resultado.getDetalles().size());
    }
}