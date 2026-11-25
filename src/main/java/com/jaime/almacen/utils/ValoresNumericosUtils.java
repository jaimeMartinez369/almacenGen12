package com.jaime.almacen.utils;

import com.jaime.almacen.exceptions.DatoInvalidoException;

import java.math.BigDecimal;

public class ValoresNumericosUtils {

    public static <N extends Number> void validarNumeroRequerido(N numero, String mensaje){
        if (numero == null){
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarEnteroPoitivo(Integer numero, String mensaje){
        validarNumeroRequerido(numero,mensaje);
        if (numero <= 0){
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarBigDecimalPositivo(BigDecimal numero, String mensaje){
        validarNumeroRequerido(numero,mensaje);

        if (numero.compareTo(BigDecimal.ZERO) <= 0){
            throw new DatoInvalidoException(mensaje);
        }
    }
}
