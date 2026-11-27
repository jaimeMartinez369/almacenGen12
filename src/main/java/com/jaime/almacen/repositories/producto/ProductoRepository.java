package com.jaime.almacen.repositories.producto;

import com.jaime.almacen.entities.Producto;
import com.jaime.almacen.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("""
            SELECT p FROM  Producto p
            WHERE (: nombre IS NULL OR
                    LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
            AND (:categoria IS NULL OR p.categoria = :categoria)
            AND (:precioMin IS NULL OR p.precio >= :precioMin)
            AND (:precioMax IS NULL OR p.precio <= :precioMax)
            """) //prueba en swagger falló,daba 200 pero no la lista en precios. corregí las or
    List<Producto> filtrarProductos(
            @Param("nombre") String nombre,
            @Param("categoria") Categoria categoria,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );
            
}