package com.Eric.ventaeventos.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Clase Usuario
 *
 * Representa a cualquier persona registrada en la plataforma.
 * Puede explorar eventos, comprar entradas y gestionar sus compras.
 *
 * Los métodos de pago son simulados, guardamos el nombre del método
 * como String (ej: "Nequi", "Tarjeta Crédito") porque no manejamos
 * datos bancarios reales en este proyecto.
 */
public class Usuario {

    private String idUsuario;
    private String nombreCompleto;
    private String correoElectronico;
    private String numeroTelefono;

    // cada usuario puede tener varios métodos de pago registrados
    private List<String> metodosDePago;

    public Usuario() {
        this.metodosDePago = new ArrayList<>();
    }

    public Usuario(String idUsuario, String nombreCompleto,
                   String correoElectronico, String numeroTelefono) {
        this.idUsuario         = idUsuario;
        this.nombreCompleto    = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.numeroTelefono    = numeroTelefono;
        this.metodosDePago     = new ArrayList<>();
    }

    // getters y setters, los necesitamos para los formularios de JavaFX
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getNumeroTelefono() { return numeroTelefono; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }

    public List<String> getMetodosDePago() { return metodosDePago; }

    public void agregarMetodoDePago(String metodo) {
        this.metodosDePago.add(metodo);
    }

    // por ahora solo imprime, más adelante se conectará con el historial
    public void registrarCompra(Compra compra) {
        System.out.println("Compra registrada para: " + nombreCompleto);
    }

    @Override
    public String toString() {
        return "Usuario: " + nombreCompleto + " (" + correoElectronico + ")";
    }
}