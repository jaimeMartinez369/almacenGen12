package com.jaime.almacen.mappers;

import com.jaime.almacen.dto.sucursales.SucursalRequest;
import com.jaime.almacen.dto.sucursales.SucursalResponse;
import com.jaime.almacen.entities.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

    public Sucursal requestAEntidad(SucursalRequest request) {
        return request == null
                ? null
                : Sucursal.crear(request.nombre(), request.direccion());
    }

    public SucursalResponse entidadAResponse(Sucursal sucursal) {
        return sucursal == null
                ? null
                : new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion()
        );
    }
}