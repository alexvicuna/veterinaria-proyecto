package com.veterinaria.pagos.dto;

import com.veterinaria.pagos.model.EstadoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDateTime fechaPago;

    @NotNull(message = "El método de pago es obligatorio")
    private EstadoPago.MetodoPago metodoPago;

    @NotNull(message = "El estado de pago es obligatorio")
    private EstadoPago.DetallePago estadoPago;
}