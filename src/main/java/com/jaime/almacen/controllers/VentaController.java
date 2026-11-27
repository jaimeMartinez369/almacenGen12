package com.jaime.almacen.controllers;

import com.jaime.almacen.docs.ProblemaDoc;
import com.jaime.almacen.dto.ventas.VentaRequest;
import com.jaime.almacen.dto.ventas.VentaResponse;
import com.jaime.almacen.services.ventas.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jaime.almacen.dto.ventas.ReporteVentasSucursalResponse;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name= "Api Ventas")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {

        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/canceladas")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {

        return ResponseEntity.ok(
                ventaService.listarCanceladas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerPorId(
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(
                ventaService.obtenerPorIdActiva(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una venta")
    @ApiResponse(responseCode = "201", description = "Venta registrada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente")
    @ApiResponse(responseCode = "404", description = "Producto o Sucursal no encontrada")

    public ResponseEntity<VentaResponse> registrar(
            @Valid @RequestBody VentaRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ventaService.registrar(request));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(
            @PathVariable @Positive Long id) {

        ventaService.cancelar(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/reporte")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> obtenerReporte() {

        return ResponseEntity.ok(
                ventaService.obtenerReportePorSucursal()
        );
    }
}