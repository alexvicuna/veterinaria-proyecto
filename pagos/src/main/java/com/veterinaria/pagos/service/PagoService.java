package com.veterinaria.pagos.service;

import com.veterinaria.pagos.dto.PagoRequestDTO;
import com.veterinaria.pagos.dto.PagoResponseDTO;
import com.veterinaria.pagos.pagosException.PagoNotFoundException;
import com.veterinaria.pagos.model.EstadoPago;
import com.veterinaria.pagos.model.Pago;
import com.veterinaria.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;

    public List<PagoResponseDTO> listarTodos() {
        return pagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PagoResponseDTO obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNotFoundException(id));
        return toResponse(pago);
    }

    public PagoResponseDTO crear(PagoRequestDTO dto) {
        Pago pago = Pago.builder()
                .monto(dto.getMonto())
                .fechaPago(dto.getFechaPago())
                .metodoPago(dto.getMetodoPago())
                .estadoPago(dto.getEstadoPago())
                .build();
        return toResponse(pagoRepository.save(pago));
    }

    public PagoResponseDTO actualizar(Long id, PagoRequestDTO dto) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNotFoundException(id));
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstadoPago(dto.getEstadoPago());
        return toResponse(pagoRepository.save(pago));
    }

    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new PagoNotFoundException(id);
        }
        pagoRepository.deleteById(id);
    }

    public List<PagoResponseDTO> listarPorEstado(EstadoPago.DetallePago detallePago) {
        return pagoRepository.findByEstadoPago(detallePago)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PagoResponseDTO> listarPorMetodoPago(EstadoPago.MetodoPago metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PagoResponseDTO> listarPorRangoDeFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pagoRepository.findByFechaPagoBetween(inicio, fin)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PagoResponseDTO toResponse(Pago pago) {
        return PagoResponseDTO.builder()
                .idPago(pago.getIdPago())
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .metodoPago(pago.getMetodoPago())
                .estadoPago(pago.getEstadoPago())
                .build();
    }
}