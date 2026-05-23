package com.veterinaria.pagos.service;

import com.veterinaria.pagos.client.CitaClient;
import com.veterinaria.pagos.dto.PagoRequestDTO;
import com.veterinaria.pagos.dto.PagoResponseDTO;
import com.veterinaria.pagos.pagosException.PagoNotFoundException;
import com.veterinaria.pagos.model.DetallePago;
import com.veterinaria.pagos.model.MetodoPago;
import com.veterinaria.pagos.model.Pago;
import com.veterinaria.pagos.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private CitaClient citaClient;

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

    public PagoResponseDTO registrarPago(PagoRequestDTO dto) {
        Pago pago = new Pago();
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setIdCita(dto.getIdCita());
        pago.setEstadoPago(DetallePago.PENDIENTE);  // ← siempre PENDIENTE al crear
        return toResponse(pagoRepository.save(pago));
    }

    public PagoResponseDTO actualizarPago(Long id, PagoRequestDTO dto) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNotFoundException(id));
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setIdCita(dto.getIdCita());
        return toResponse(pagoRepository.save(pago));
    }


    public PagoResponseDTO actualizarEstado(Long id, DetallePago detallePago) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNotFoundException(id));
        pago.setEstadoPago(detallePago);
        return toResponse(pagoRepository.save(pago));
    }

    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new PagoNotFoundException(id);
        }
        pagoRepository.deleteById(id);
    }

    public List<PagoResponseDTO> listarPorEstado(DetallePago detallePago) {
        return pagoRepository.findByEstadoPago(detallePago)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PagoResponseDTO> listarPorMetodoPago(MetodoPago metodoPago) {
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
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setMonto(pago.getMonto());
        dto.setFechaPago(pago.getFechaPago());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setCita(citaClient.obtenerCita(pago.getIdCita()));
        return dto;
    }
}