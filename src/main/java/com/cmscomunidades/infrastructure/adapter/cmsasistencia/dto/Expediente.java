package com.cmscomunidades.infrastructure.adapter.cmsasistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expediente {
    private String nroServicio;
    private String nroEncargo;
    private boolean urgente;
    private String nombreApellidos;
    private String nif;
    private String direccion;
    private long cp;
    private String localidad;
    private String provincia;
    private String telefono1;
    private String telefono2;
    private String telefono3;
    private String email;
    private String observaciones;
    private String poliza;
    private String numeroSerie;
    private String marca;
    private String modelo;
    private LocalDate fechaCompra;
    private String idServicio;
    private String tienda;
    private String servicio;
    private String producto;
}