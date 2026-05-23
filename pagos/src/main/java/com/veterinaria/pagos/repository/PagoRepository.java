package com.veterinaria.pagos.repository;

import com.veterinaria.pagos.model.DetallePago;
import com.veterinaria.pagos.model.MetodoPago;
import com.veterinaria.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByEstadoPago(DetallePago detallePago);

    List<Pago> findByMetodoPago(MetodoPago metodoPago);

    List<Pago> findByFechaPagoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}