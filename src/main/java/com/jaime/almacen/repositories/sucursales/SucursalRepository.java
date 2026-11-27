package com.jaime.almacen.repositories.sucursales;

import com.jaime.almacen.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    //SELECT COUNT(*) FROM SUCURSALES WHERE NOMBRE =?;
    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);


}
