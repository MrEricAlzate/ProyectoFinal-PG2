package com.Eric.ventaeventos.model;

import java.time.LocalDateTime;

/*
 * Clase Incidencia
 *
 * Sirve para registrar cuando algo sale mal o fuera de lo normal
 * en la plataforma. Por ejemplo si alguien intenta comprar un asiento
 * que ya estaba vendido, o si un pago falla, o si el admin cancela
 * un evento masivamente.
 *
 * La idea es que el admin pueda consultar estas incidencias después
 * para saber qué pasó y cuándo (RF-042 pide filtrar por fecha y tipo).
 *
 * Los tipos que manejamos por ahora son:
 * ERROR_PAGO, DOBLE_COMPRA, CANCELACION_MASIVA, CANCELACION y OTRO
 */
public class Incidencia {

    private String idIncidencia;
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;

    // para saber a qué le pasó: puede ser un EVENTO, una COMPRA o un USUARIO
    private String entidadAfectada;
    private String idEntidadAfectada;  // el id específico de esa entidad

    // la fecha se registra automáticamente cuando se crea la incidencia
    public Incidencia() {
        this.fecha = LocalDateTime.now();
    }

    public Incidencia(String idIncidencia, String tipo,
                      String descripcion, String entidadAfectada,
                      String idEntidadAfectada) {
        this();
        this.idIncidencia      = idIncidencia;
        this.tipo              = tipo;
        this.descripcion       = descripcion;
        this.entidadAfectada   = entidadAfectada;
        this.idEntidadAfectada = idEntidadAfectada;
    }

    public String getIdIncidencia() { return idIncidencia; }
    public void setIdIncidencia(String idIncidencia) { this.idIncidencia = idIncidencia; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEntidadAfectada() { return entidadAfectada; }
    public void setEntidadAfectada(String entidadAfectada) { this.entidadAfectada = entidadAfectada; }

    public String getIdEntidadAfectada() { return idEntidadAfectada; }
    public void setIdEntidadAfectada(String id) { this.idEntidadAfectada = id; }

    @Override
    public String toString() {
        return "[" + tipo + "] " + descripcion + " — " + fecha;
    }
}