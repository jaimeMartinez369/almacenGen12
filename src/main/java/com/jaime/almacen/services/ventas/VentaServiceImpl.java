package com.jaime.almacen.services.ventas;

import com.jaime.almacen.dto.ventas.DetalleVentasRequest;
import com.jaime.almacen.dto.ventas.VentaRequest;
import com.jaime.almacen.dto.ventas.VentaResponse;
import com.jaime.almacen.entities.DetalleVenta;
import com.jaime.almacen.entities.Producto;
import com.jaime.almacen.entities.Sucursal;
import com.jaime.almacen.entities.Venta;
import com.jaime.almacen.enums.EstadoVenta;
import com.jaime.almacen.exceptions.RecursoNoEncontradoException;
import com.jaime.almacen.mappers.VentaMapper;
import com.jaime.almacen.repositories.producto.ProductoRepository;
import com.jaime.almacen.repositories.sucursales.SucursalRepository;
import com.jaime.almacen.repositories.venta.VentaRepository;
import com.jaime.almacen.dto.ventas.ReporteVentasSucursalResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaMapper ventaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {

        log.info("Listando ventas registradas");

        return ventaRepository
                .findAllByEstadoVenta(EstadoVenta.REGISTRADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {

        log.info("Listando ventas canceladas");

        return ventaRepository
                .findAllByEstadoVenta(EstadoVenta.CANCELADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {

        log.info("Buscando venta activa con id: {}", id);

        Venta venta = ventaRepository
                .findByIdAndEstadoVenta(
                        id, EstadoVenta.REGISTRADA)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Venta activa no encontrada con id: " + id));

        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {

        log.info("Registrando nueva venta");

        Sucursal sucursal = sucursalRepository
                .findById(request.idSucursal())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Sucursal no encontrada con id: "
                                        + request.idSucursal()));

        Venta venta = Venta.builder()
                .fecha(LocalDate.now())
                .estadoVenta(EstadoVenta.REGISTRADA)
                .sucursal(sucursal)
                .build();

        for (DetalleVentasRequest detalleRequest : request.productos()) {

            Producto producto = productoRepository
                    .findById(detalleRequest.idProducto())
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException(
                                    "Producto no encontrado con id: "
                                            + detalleRequest.idProducto()));

            producto.descontarCantidad(
                    detalleRequest.cantidadProducto());

            DetalleVenta detalle = DetalleVenta.builder()
                    .producto(producto)
                    .cantidadProducto(
                            detalleRequest.cantidadProducto())
                    .precioProducto(producto.getPrecio())
                    .build();

            venta.agregarDetalle(detalle);
        }

        ventaRepository.saveAndFlush(venta);

        log.info("Venta registrada con id: {}", venta.getId());

        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public void cancelar(Long id) {

        log.info("Cancelando venta con id: {}", id);

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Venta no encontrada con id: " + id));

        venta.cancelar();

        for (DetalleVenta detalle : venta.getDetalleVentas()) {

            detalle.getProducto().aumentarCantidad(
                    detalle.getCantidadProducto());
        }

        ventaRepository.saveAndFlush(venta);

        log.info("Venta cancelada con id: {}", id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> obtenerReportePorSucursal() {

        log.info("Generando reporte financiero por sucursal");

        return ventaRepository.obtenerReportePorSucursal(
                EstadoVenta.REGISTRADA
        );
    }

}