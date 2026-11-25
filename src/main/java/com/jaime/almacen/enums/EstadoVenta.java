package com.jaime.almacen.enums;

import com.jaime.almacen.exceptions.DatoInvalidoException;
import com.jaime.almacen.utils.StringCustomUtils;
import com.jaime.almacen.utils.ValoresNumericosUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum EstadoVenta {

    REGISTRADA(1l, "Registrada"),
    CANCELADA(0L, "Cancelada");

    private final Long codigo;
    private final String descripcion;

    public static EstadoVenta obtenerCategoriaPorDescripcion(String descripcion) {
        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (EstadoVenta estadoVenta : values()) {
            if (StringCustomUtils.normalizarTexto(estadoVenta.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return estadoVenta;
        }
        throw new DatoInvalidoException("No existe una categoria con la descripción: " + descripcion);
    }

    public static EstadoVenta obtenerCategoriaPorCodigo(Long codigo) {
        ValoresNumericosUtils.validarNumeroRequerido(codigo, "El codigo es requerido");
        for (EstadoVenta estadoVenta : values()) {
            if (estadoVenta.codigo.equals((codigo)))
                return estadoVenta;
        }
        throw new DatoInvalidoException("No existe un dato de venta con el código: " + codigo);
    }
}