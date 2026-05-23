package com.veterinaria.boleta.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoletaResponseDTO {

    private Long idBoleta;
    private LocalDate fecha;
    private Double total;
    private CitaDTO cita;
    private PagoDTO pago;
    private List<DetalleBoletaResponseDTO> detalles;
}