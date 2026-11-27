package com.jaime.almacen.mappers;

import com.jaime.almacen.dto.ventas.DetalleVentasResponse;
import com.jaime.almacen.dto.ventas.VentaResponse;
import com.jaime.almacen.entities.DetalleVenta;
import com.jaime.almacen.entities.Venta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VentaMapper {

    private final SucursalMapper sucursalMapper;

    public DetalleVentasResponse detalleAResponse(
            DetalleVenta detalle) {

        BigDecimal subtotal = detalle.getPrecioProducto()
                .multiply(BigDecimal.valueOf(
                        detalle.getCantidadProducto()));

        return new DetalleVentasResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidadProducto(),
                detalle.getPrecioProducto(),
                subtotal
        );
    }

    public VentaResponse entidadAResponse(Venta venta) {

        List<DetalleVentasResponse> detalles =
                venta.getDetalleVentas()
                        .stream()
                        .map(this::detalleAResponse)
                        .toList();

        BigDecimal total = detalles.stream()
                .map(DetalleVentasResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentaResponse(
                venta.getId(),
                venta.getFecha().toString(),
                venta.getEstadoVenta().name(),
                sucursalMapper.entidadAResponse(
                        venta.getSucursal()),
                detalles,
                total
        );
    }
}