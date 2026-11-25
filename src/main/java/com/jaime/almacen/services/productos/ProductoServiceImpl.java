package com.jaime.almacen.services.productos;

import com.jaime.almacen.dto.productos.ProductoRequest;
import com.jaime.almacen.dto.productos.ProductoResponse;
import com.jaime.almacen.entities.Producto;
import com.jaime.almacen.enums.Categoria;
import com.jaime.almacen.exceptions.RecursoNoEncontradoException;
import com.jaime.almacen.mappers.ProductoMapper;
import com.jaime.almacen.repositories.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(String nombre, String categoria, BigDecimal precioMin, BigDecimal precioMax) {
        log.info("Listando todos los productos");
        return productoRepository.findAll().stream()
                .filter(p -> nombre == null ||
                        p.getNombre().toLowerCase()
                                .contains(nombre.toLowerCase()))
                .filter(p -> precioMin == null ||
                        p.getPrecio().compareTo(precioMin) >= 0)
                .filter(p -> precioMax == null ||
                        p.getPrecio().compareTo(precioMax) <= 0)
                .filter(p -> categoria == null ||
                        p.getCategoria() == Categoria.obtenerCategoriaPorDescripcion(categoria))
                .map(productoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoOException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto...");
        Producto producto= productoMapper.requestAEntidad(
                request, Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()));
        productoRepository.saveAndFlush(producto);
        log.info("nuevo producto {} registrado", producto.getNombre());
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = obtenerProductoOException(id);
        log.info("actualizando producto con id: {}", id);

        producto.actualizar(
                request.nombre().trim(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()),
                request.precio(),
                request.cantidad());
        productoRepository.saveAndFlush(producto);
        log.info("producto con id {} actualizado", id);

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
    Producto producto = obtenerProductoOException(id);
    log.info("Eliminando producto con id {}", id);
    productoRepository.delete(producto);
    productoRepository.flush();
    log.info("producto con id {} eliminado", id);
    }

    private Producto obtenerProductoOException(Long id){
        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id)
        );
    }
}
