package com.veterinaria.pagos.dto;

import com.veterinaria.pagos.model.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDTO {

    private Long idPago;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private EstadoPago.MetodoPago metodoPago;
    private EstadoPago.DetallePago estadoPago;
}