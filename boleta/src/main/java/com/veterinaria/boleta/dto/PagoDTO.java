package com.veterinaria.boleta.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class PagoDTO {
    private Long idPago;
    private BigDecimal monto;
    private LocalDate fechaPago;
    private String metodoPago;
    private String estadoPago;
}