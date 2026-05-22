package com.veterinaria.inventario.repository;

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