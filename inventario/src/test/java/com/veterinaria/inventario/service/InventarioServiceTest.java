package com.veterinaria.inventario.service;

import com.veterinaria.inventario.dto.InventarioRequestDTO;
import com.veterinaria.inventario.dto.InventarioResponseDTO;
import com.veterinaria.inventario.inventarioException.InventarioNotFoundException;
import com.veterinaria.inventario.model.Inventario;
import com.veterinaria.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - InventarioService")
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario productoGuardado;
    private InventarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        productoGuardado = new Inventario();
        productoGuardado.setIdProducto(1L);
        productoGuardado.setNombreProducto("Amoxicilina");
        productoGuardado.setDescripcion("Antibiótico de amplio espectro");
        productoGuardado.setCategoriaProducto("MEDICAMENTO");
        productoGuardado.setCantidadDisponible(50);

        requestDTO = new InventarioRequestDTO();
        requestDTO.setNombreProducto("Amoxicilina");
        requestDTO.setDescripcion("Antibiótico de amplio espectro");
        requestDTO.setCategoriaProducto("MEDICAMENTO");
        requestDTO.setCantidadDisponible(50);
    }

    @Test
    @DisplayName("crearProducto - debe guardar y retornar el producto correctamente")
    void crearProducto_cuandoDatosValidos_debeRetornarResponseDTO() {
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(productoGuardado);
        InventarioResponseDTO resultado = inventarioService.crearProducto(requestDTO);
        assertNotNull(resultado);
        assertEquals("Amoxicilina", resultado.getNombreProducto());
        assertEquals("MEDICAMENTO", resultado.getCategoriaProducto());
        assertEquals(50, resultado.getCantidadDisponible());
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    @DisplayName("crearProducto - debe mapear todos los campos del request al modelo")
    void crearProducto_debeMapearCamposCorrectamente() {
        requestDTO.setNombreProducto("Vacuna Antirrábica");
        requestDTO.setCategoriaProducto("VACUNA");
        requestDTO.setCantidadDisponible(20);
        Inventario vacuna = new Inventario();
        vacuna.setIdProducto(2L);
        vacuna.setNombreProducto("Vacuna Antirrábica");
        vacuna.setCategoriaProducto("VACUNA");
        vacuna.setCantidadDisponible(20);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(vacuna);
        InventarioResponseDTO resultado = inventarioService.crearProducto(requestDTO);
        assertEquals("VACUNA", resultado.getCategoriaProducto());
        assertEquals(20, resultado.getCantidadDisponible());
    }

    @Test
    @DisplayName("obtenerTodos - debe retornar lista con todos los productos")
    void obtenerTodos_cuandoExistenProductos_debeRetornarLista() {
        Inventario otro = new Inventario();
        otro.setIdProducto(2L);
        otro.setNombreProducto("Vacuna");
        otro.setCategoriaProducto("VACUNA");
        otro.setCantidadDisponible(20);
        when(inventarioRepository.findAll()).thenReturn(List.of(productoGuardado, otro));
        List<InventarioResponseDTO> resultado = inventarioService.obtenerTodos();
        assertEquals(2, resultado.size());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos - debe retornar lista vacía cuando no hay productos")
    void obtenerTodos_cuandoNoExistenProductos_debeRetornarListaVacia() {
        when(inventarioRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(inventarioService.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - debe retornar el producto cuando existe el ID")
    void obtenerPorId_cuandoExisteId_debeRetornarProducto() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoGuardado));
        InventarioResponseDTO resultado = inventarioService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdProducto());
        assertEquals("Amoxicilina", resultado.getNombreProducto());
    }

    @Test
    @DisplayName("obtenerPorId - debe lanzar InventarioNotFoundException cuando el ID no existe")
    void obtenerPorId_cuandoNoExisteId_debeLanzarExcepcion() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());
        InventarioNotFoundException ex = assertThrows(
                InventarioNotFoundException.class,
                () -> inventarioService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("obtenerPorNombre - debe retornar productos que coincidan con el nombre")
    void obtenerPorNombre_cuandoExistenCoincidencias_debeRetornarLista() {
        when(inventarioRepository.findByNombreProductoContainingIgnoreCase("amoxi"))
                .thenReturn(List.of(productoGuardado));
        List<InventarioResponseDTO> resultado = inventarioService.obtenerPorNombre("amoxi");
        assertEquals(1, resultado.size());
        assertEquals("Amoxicilina", resultado.get(0).getNombreProducto());
    }

    @Test
    @DisplayName("obtenerPorNombre - debe retornar vacío si no hay coincidencias")
    void obtenerPorNombre_cuandoNoHayCoincidencias_debeRetornarListaVacia() {
        when(inventarioRepository.findByNombreProductoContainingIgnoreCase("xyz"))
                .thenReturn(Collections.emptyList());
        assertTrue(inventarioService.obtenerPorNombre("xyz").isEmpty());
    }

    @Test
    @DisplayName("obtenerPorCategoria - debe retornar productos de la categoría indicada")
    void obtenerPorCategoria_cuandoExistenProductos_debeRetornarLista() {
        when(inventarioRepository.findByCategoriaProducto("MEDICAMENTO"))
                .thenReturn(List.of(productoGuardado));
        List<InventarioResponseDTO> resultado = inventarioService.obtenerPorCategoria("MEDICAMENTO");
        assertEquals(1, resultado.size());
        assertEquals("MEDICAMENTO", resultado.get(0).getCategoriaProducto());
    }

    @Test
    @DisplayName("obtenerPorCategoria - debe retornar vacío si no hay productos en esa categoría")
    void obtenerPorCategoria_cuandoNoHayProductos_debeRetornarListaVacia() {
        when(inventarioRepository.findByCategoriaProducto("ALIMENTO"))
                .thenReturn(Collections.emptyList());
        assertTrue(inventarioService.obtenerPorCategoria("ALIMENTO").isEmpty());
    }

    @Test
    @DisplayName("obtenerStockBajo - debe retornar productos con stock menor al indicado")
    void obtenerStockBajo_cuandoExistenProductos_debeRetornarLista() {
        Inventario stockBajo = new Inventario();
        stockBajo.setIdProducto(3L);
        stockBajo.setNombreProducto("Jeringa 5ml");
        stockBajo.setCategoriaProducto("INSUMO_CLINICO");
        stockBajo.setCantidadDisponible(3);
        when(inventarioRepository.findByCantidadDisponibleLessThan(10))
                .thenReturn(List.of(stockBajo));
        List<InventarioResponseDTO> resultado = inventarioService.obtenerStockBajo(10);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getCantidadDisponible() < 10);
    }

    @Test
    @DisplayName("obtenerStockBajo - debe retornar vacío si todos tienen stock suficiente")
    void obtenerStockBajo_cuandoNoHayStockBajo_debeRetornarListaVacia() {
        when(inventarioRepository.findByCantidadDisponibleLessThan(5))
                .thenReturn(Collections.emptyList());
        assertTrue(inventarioService.obtenerStockBajo(5).isEmpty());
    }

    @Test
    @DisplayName("actualizarProducto - debe modificar los datos cuando existe el producto")
    void actualizarProducto_cuandoExisteProducto_debeActualizarYRetornar() {
        InventarioRequestDTO req = new InventarioRequestDTO();
        req.setNombreProducto("Amoxicilina 500mg");
        req.setDescripcion("Actualizado");
        req.setCategoriaProducto("MEDICAMENTO");
        req.setCantidadDisponible(100);
        Inventario actualizado = new Inventario();
        actualizado.setIdProducto(1L);
        actualizado.setNombreProducto("Amoxicilina 500mg");
        actualizado.setDescripcion("Actualizado");
        actualizado.setCategoriaProducto("MEDICAMENTO");
        actualizado.setCantidadDisponible(100);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoGuardado));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(actualizado);
        InventarioResponseDTO resultado = inventarioService.actualizarProducto(1L, req);
        assertEquals("Amoxicilina 500mg", resultado.getNombreProducto());
        assertEquals(100, resultado.getCantidadDisponible());
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    @DisplayName("actualizarProducto - debe lanzar excepción cuando el producto no existe")
    void actualizarProducto_cuandoNoExisteProducto_debeLanzarExcepcion() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> inventarioService.actualizarProducto(99L, requestDTO));
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    @DisplayName("eliminarProducto - debe eliminar el producto cuando existe")
    void eliminarProducto_cuandoExisteId_debeEliminar() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoGuardado));
        doNothing().when(inventarioRepository).delete(any(Inventario.class));
        inventarioService.eliminarProducto(1L);
        verify(inventarioRepository, times(1)).delete(productoGuardado);
    }

    @Test
    @DisplayName("eliminarProducto - debe lanzar excepción cuando el ID no existe")
    void eliminarProducto_cuandoNoExisteId_debeLanzarExcepcion() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventarioService.eliminarProducto(99L));
        assertTrue(ex.getMessage().contains("99"));
        verify(inventarioRepository, never()).delete(any(Inventario.class));
    }
}