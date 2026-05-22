package com.veterinaria.inventario.repository;

<<<<<<< HEAD
public interface InventarioRepository {
}
=======
import com.veterinaria.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByNombreProductoContainingIgnoreCase(String nombreProducto);

    List<Inventario> findByCategoriaProducto(String categoriaProducto);

    List<Inventario> findByCantidadDisponibleLessThan(Integer cantidad);
}
>>>>>>> 0429cfed3641891bf219397071c83ecf49cf9344
