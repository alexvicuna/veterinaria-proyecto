package com.veterinaria.pagos;

import com.veterinaria.pagos.client.CitaClient;
import com.veterinaria.pagos.dto.CitaDTO;
import com.veterinaria.pagos.dto.PagoRequestDTO;
import com.veterinaria.pagos.dto.PagoResponseDTO;
import com.veterinaria.pagos.model.DetallePago;
import com.veterinaria.pagos.model.MetodoPago;
import com.veterinaria.pagos.model.Pago;
import com.veterinaria.pagos.pagosException.PagoNotFoundException;
import com.veterinaria.pagos.repository.PagoRepository;
import com.veterinaria.pagos.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private CitaClient citaClient;

    @InjectMocks
    private PagoService pagoService;

    private Pago pagoEjemplo;
    private PagoRequestDTO requestDTO;
    private CitaDTO citaDTO;

    @BeforeEach
    void setUp() {
        pagoEjemplo = new Pago();
        pagoEjemplo.setIdPago(1L);
        pagoEjemplo.setMonto(new BigDecimal("50000.00"));
        pagoEjemplo.setFechaPago(LocalDateTime.of(2025, 6, 1, 10, 0));
        pagoEjemplo.setMetodoPago(MetodoPago.EFECTIVO);
        pagoEjemplo.setEstadoPago(DetallePago.PENDIENTE);
        pagoEjemplo.setIdCita(1L);

        requestDTO = new PagoRequestDTO();
        requestDTO.setMonto(new BigDecimal("50000.00"));
        requestDTO.setFechaPago(LocalDateTime.of(2025, 6, 1, 10, 0));
        requestDTO.setMetodoPago(MetodoPago.EFECTIVO);
        requestDTO.setIdCita(1L);

        citaDTO = new CitaDTO();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 1: registrarPago → estado inicial siempre PENDIENTE
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void registrarPago_debeCrearPagoConEstadoPendiente() {
        // GIVEN
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoEjemplo);
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // WHEN
        PagoResponseDTO resultado = pagoService.registrarPago(requestDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals(DetallePago.PENDIENTE, resultado.getEstadoPago());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 2: registrarPago → el monto se guarda correctamente
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void registrarPago_debeGuardarMontoCorrectamente() {
        // GIVEN
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoEjemplo);
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // WHEN
        PagoResponseDTO resultado = pagoService.registrarPago(requestDTO);

        // THEN
        assertEquals(new BigDecimal("50000.00"), resultado.getMonto());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 3: obtenerPorId → retorna el pago cuando existe
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void obtenerPorId_cuandoExiste_debeRetornarPago() {
        // GIVEN
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoEjemplo));
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // WHEN
        PagoResponseDTO resultado = pagoService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPago());
        assertEquals(MetodoPago.EFECTIVO, resultado.getMetodoPago());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 4: obtenerPorId → lanza excepción cuando NO existe
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarPagoNotFoundException() {
        // GIVEN
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(PagoNotFoundException.class, () -> pagoService.obtenerPorId(99L));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 5: listarTodos → retorna lista con todos los pagos
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void listarTodos_debeRetornarListaDePagos() {
        // GIVEN
        Pago otroPago = new Pago();
        otroPago.setIdPago(2L);
        otroPago.setMonto(new BigDecimal("30000.00"));
        otroPago.setFechaPago(LocalDateTime.now());
        otroPago.setMetodoPago(MetodoPago.TRANSFERENCIA);
        otroPago.setEstadoPago(DetallePago.COMPLETADO);
        otroPago.setIdCita(2L);

        when(pagoRepository.findAll()).thenReturn(List.of(pagoEjemplo, otroPago));
        when(citaClient.obtenerCita(anyLong())).thenReturn(citaDTO);

        // WHEN
        List<PagoResponseDTO> resultado = pagoService.listarTodos();

        // THEN
        assertEquals(2, resultado.size());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 6: actualizarEstado → cambia el estado correctamente
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void actualizarEstado_debeActualizarEstadoDelPago() {
        // GIVEN
        Pago pagoActualizado = new Pago();
        pagoActualizado.setIdPago(1L);
        pagoActualizado.setMonto(new BigDecimal("50000.00"));
        pagoActualizado.setFechaPago(LocalDateTime.of(2025, 6, 1, 10, 0));
        pagoActualizado.setMetodoPago(MetodoPago.EFECTIVO);
        pagoActualizado.setEstadoPago(DetallePago.COMPLETADO);
        pagoActualizado.setIdCita(1L);

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoEjemplo));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoActualizado);
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // WHEN
        PagoResponseDTO resultado = pagoService.actualizarEstado(1L, DetallePago.COMPLETADO);

        // THEN
        assertEquals(DetallePago.COMPLETADO, resultado.getEstadoPago());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 7: actualizarEstado → lanza excepción si el pago no existe
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void actualizarEstado_cuandoNoExiste_debeLanzarExcepcion() {
        // GIVEN
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(PagoNotFoundException.class,
                () -> pagoService.actualizarEstado(99L, DetallePago.COMPLETADO));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 8: eliminarPago → elimina cuando existe
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void eliminarPago_cuandoExiste_debeEliminar() {
        // GIVEN
        when(pagoRepository.existsById(1L)).thenReturn(true);

        // WHEN
        pagoService.eliminarPago(1L);

        // THEN
        verify(pagoRepository, times(1)).deleteById(1L);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 9: eliminarPago → lanza excepción si no existe
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void eliminarPago_cuandoNoExiste_debeLanzarExcepcion() {
        // GIVEN
        when(pagoRepository.existsById(99L)).thenReturn(false);

        // WHEN - THEN
        assertThrows(PagoNotFoundException.class, () -> pagoService.eliminarPago(99L));
        verify(pagoRepository, never()).deleteById(anyLong());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 10: listarPorEstado → filtra correctamente por estado
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void listarPorEstado_debeRetornarPagosConEseEstado() {
        // GIVEN
        when(pagoRepository.findByEstadoPago(DetallePago.PENDIENTE))
                .thenReturn(List.of(pagoEjemplo));
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // WHEN
        List<PagoResponseDTO> resultado = pagoService.listarPorEstado(DetallePago.PENDIENTE);

        // THEN
        assertEquals(1, resultado.size());
        assertEquals(DetallePago.PENDIENTE, resultado.get(0).getEstadoPago());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 11: listarPorMetodoPago → filtra por método de pago
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void listarPorMetodoPago_debeRetornarPagosConEseMetodo() {
        // GIVEN
        when(pagoRepository.findByMetodoPago(MetodoPago.EFECTIVO))
                .thenReturn(List.of(pagoEjemplo));
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // WHEN
        List<PagoResponseDTO> resultado = pagoService.listarPorMetodoPago(MetodoPago.EFECTIVO);

        // THEN
        assertEquals(1, resultado.size());
        assertEquals(MetodoPago.EFECTIVO, resultado.get(0).getMetodoPago());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 12: listarPorRangoDeFechas → retorna pagos dentro del rango
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void listarPorRangoDeFechas_debeRetornarPagosDentroDelRango() {
        // GIVEN
        LocalDateTime inicio = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2025, 12, 31, 23, 59);

        when(pagoRepository.findByFechaPagoBetween(inicio, fin))
                .thenReturn(List.of(pagoEjemplo));
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // WHEN
        List<PagoResponseDTO> resultado = pagoService.listarPorRangoDeFechas(inicio, fin);

        // THEN
        assertEquals(1, resultado.size());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 13: actualizarPago → actualiza los campos del pago
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void actualizarPago_debeActualizarCamposDelPago() {
        // GIVEN
        PagoRequestDTO dtoActualizado = new PagoRequestDTO();
        dtoActualizado.setMonto(new BigDecimal("80000.00"));
        dtoActualizado.setFechaPago(LocalDateTime.of(2025, 7, 1, 12, 0));
        dtoActualizado.setMetodoPago(MetodoPago.TARJETA_CREDITO);
        dtoActualizado.setIdCita(1L);

        Pago pagoModificado = new Pago();
        pagoModificado.setIdPago(1L);
        pagoModificado.setMonto(new BigDecimal("80000.00"));
        pagoModificado.setFechaPago(LocalDateTime.of(2025, 7, 1, 12, 0));
        pagoModificado.setMetodoPago(MetodoPago.TARJETA_CREDITO);
        pagoModificado.setEstadoPago(DetallePago.PENDIENTE);
        pagoModificado.setIdCita(1L);

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoEjemplo));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoModificado);
        when(citaClient.obtenerCita(1L)).thenReturn(citaDTO);

        // WHEN
        PagoResponseDTO resultado = pagoService.actualizarPago(1L, dtoActualizado);

        // THEN
        assertEquals(new BigDecimal("80000.00"), resultado.getMonto());
        assertEquals(MetodoPago.TARJETA_CREDITO, resultado.getMetodoPago());
    }
}