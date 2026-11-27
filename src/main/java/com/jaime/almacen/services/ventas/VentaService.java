package com.jaime.almacen.services.ventas;

import com.jaime.almacen.dto.ventas.VentaRequest;
import com.jaime.almacen.dto.ventas.VentaResponse;
import com.jaime.almacen.dto.ventas.ReporteVentasSucursalResponse;

import java.util.List;

public interface VentaService {

    List<VentaResponse> listar();

    List<VentaResponse> listarCanceladas();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    void cancelar(Long id);

    List<ReporteVentasSucursalResponse> obtenerReportePorSucursal();
}