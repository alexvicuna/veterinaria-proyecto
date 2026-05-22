package com.veterinaria.pagos.model;

public class EstadoPago {


    public enum MetodoPago {
        EFECTIVO,
        TARJETA_DEBITO,
        TARJETA_CREDITO,
        TRANSFERENCIA
    }

    public enum DetallePago {
        PENDIENTE,
        COMPLETADO,
        RECHAZADO,
        REEMBOLSADO
    }

}
