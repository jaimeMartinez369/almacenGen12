package com.jaime.almacen.repositories.venta;

import com.jaime.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.jaime.almacen.entities.Venta;
import com.jaime.almacen.enums.EstadoVenta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findAllByEstadoVenta(EstadoVenta estadoVenta);

    Optional<Venta> findByIdAndEstadoVenta(
            Long id,
            EstadoVenta estadoVenta
    );

    @Query("""
            SELECT new com.jaime.almacen.dto.ventas.ReporteVentasSucursalResponse(
                v.sucursal.id,
                v.sucursal.nombre,
                SUM(d.cantidadProducto * d.precioProducto),
                SUM(d.cantidadProducto)
            )
            FROM Venta v
            JOIN v.detalleVentas d
            WHERE v.estadoVenta = :estado
            GROUP BY v.sucursal.id, v.sucursal.nombre
            """)
    List<ReporteVentasSucursalResponse> obtenerReportePorSucursal(
            @Param("estado") EstadoVenta estado
    );
}