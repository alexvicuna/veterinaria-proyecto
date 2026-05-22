package com.veterinaria.pagos.pagosException;

public class PagoNotFoundException extends RuntimeException {

  public PagoNotFoundException(Long id) {
    super("Pago no encontrado con ID: " + id);
  }

}