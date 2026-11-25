
package com.jaime.almacen.controllers;

import com.jaime.almacen.dto.productos.ProductoRequest;
import com.jaime.almacen.dto.productos.ProductoResponse;
import com.jaime.almacen.services.productos.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Validated
@Tag(name = "API Productos", description = "Gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    // Listar productos con filtros opcionales
    @GetMapping
    @Operation(
            summary = "Listar productos",
            description = "Todos los filtros son opcionales"
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    public ResponseEntity<List<ProductoResponse>> listar(

            @Parameter(description = "Búsqueda por nombre", example = "Laptop")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Búsqueda por categoría", example = "Electrónico")
            @RequestParam(required = false) String categoria,

            @Parameter(description = "Precio mínimo", example = "1000")
            @RequestParam(required = false) BigDecimal precioMin,

            @Parameter(description = "Precio máximo", example = "20000")
            @RequestParam(required = false) BigDecimal precioMax

    ) {
        return ResponseEntity.ok(
                productoService.listar(nombre, categoria, precioMin, precioMax)
        );
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ResponseEntity<ProductoResponse> obtenerPorId(

            @Parameter(description = "ID del producto", example = "1")
            @PathVariable
            @Positive(message = "El ID debe ser positivo")
            Long id

    ) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // Registrar un nuevo producto
    @PostMapping
    @Operation(summary = "Registrar un nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto creado correctamente")
    public ResponseEntity<ProductoResponse> registrar(

            @Valid @RequestBody ProductoRequest request

    ) {
        ProductoResponse producto = productoService.registrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    // Actualizar producto existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ResponseEntity<ProductoResponse> actualizar(

            @Parameter(description = "ID del producto", example = "1")
            @PathVariable
            @Positive(message = "El ID debe ser positivo")
            Long id,

            @Valid @RequestBody ProductoRequest request

    ) {
        return ResponseEntity.ok(
                productoService.actualizar(request, id)
        );
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ResponseEntity<Void> eliminar(

            @Parameter(description = "ID del producto", example = "1")
            @PathVariable
            @Positive(message = "El ID debe ser positivo")
            Long id

    ) {
        productoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
