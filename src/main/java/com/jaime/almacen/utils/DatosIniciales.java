package com.jaime.almacen.utils;

import com.jaime.almacen.entities.Producto;
import com.jaime.almacen.entities.Sucursal;
import com.jaime.almacen.enums.Categoria;
import com.jaime.almacen.repositories.sucursales.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.jaime.almacen.repositories.producto.ProductoRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j


public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    public void run(String... args) throws Exception {

        if(productoRepository.count()==0){
            productoRepository.saveAll(List.of(
                            new Producto(
                                    null,
                                    "Laptop Gamer",
                                    Categoria.ELECTRONICA,
                                    BigDecimal.valueOf(15000),
                                    10),

                            new Producto(
                                    null,
                                    "Mouse Inalambrico",
                                    Categoria.ELECTRONICA,
                                    BigDecimal.valueOf(25),
                                    50),

                            new Producto(
                                    null,
                                    "Camiseta Deportiva",
                                    Categoria.ROPA,
                                    BigDecimal.valueOf(20),
                                    100)
                    )
            );
            log.info("Productos de prueba cargados correctamente");

        }

        if(sucursalRepository.count() == 0){

            sucursalRepository.saveAll(List.of(
                    new Sucursal(
                            null,
                            "Sucursal Norte",
                            "Av principal 123"
                    ),

                    new Sucursal(
                            null,
                            "Sucursal central",
                            "Av principal 123"
                    ),

                    new Sucursal(
                            null,
                            "Sucursal sur",
                            "calle sur 234"
                    )
            ));
            log.info("Sucursales de prueba cargados correctamente");

        }

    }
}