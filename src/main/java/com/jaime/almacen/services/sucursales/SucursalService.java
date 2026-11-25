package com.jaime.almacen.services.sucursales;

import com.jaime.almacen.dto.sucursales.SucursalRequest;
import com.jaime.almacen.dto.sucursales.SucursalResponse;

import java.util.List;

public interface SucursalService {

    List<SucursalResponse> listar();

    SucursalResponse obtenerPorId(Long id);

    SucursalResponse registrar(SucursalRequest request);

    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);

}