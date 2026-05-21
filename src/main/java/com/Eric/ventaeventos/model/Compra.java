package com.Eric.ventaeventos.model;

import com.Eric.ventaeventos.service.CompraComponent;
import com.Eric.ventaeventos.service.CompraBase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Clase Compra
 *
 * Representa una compra que hace un usuario en la plataforma.
 * Al principio teníamos toda la lógica aquí (cancelar, pagar, calcular)
 * pero nos dimos cuenta que violaba SRP porque la clase hacía demasiadas cosas.
 * Entonces movimos la lógica de cancelación a CompraService y dejamos
 * esta clase solo con los datos. Así es más fácil de mantener.
 */
public class Compra {

    private String idCompra;
    private Usuario usuario;       // quién compró
    private Evento evento;         // a qué evento
    private LocalDateTime fechaCreacion;
    private double totalBase;      // precio sin servicios extra
    private String estado;         // Creada, Pagada, Confirmada, Cancelada...

    private List<Entrada> entradas;

    // esto es para el patrón Decorator, permite ir agregando servicios
    // como VIP o seguro de cancelación encima del precio base
    private CompraComponent componenteActual;

    // constructor vacío, el estado inicial siempre es "Creada"
    public Compra() {
        this.fechaCreacion    = LocalDateTime.now();
        this.entradas         = new ArrayList<>();
        this.estado           = "Creada";
        this.componenteActual = new CompraBase(this);
    }

    // este es el que más usamos, recibe el usuario y el evento
    public Compra(Usuario usuario, Evento evento) {
        this();
        this.usuario = usuario;
        this.evento  = evento;
    }

    // cuando el usuario agrega un servicio extra (VIP, seguro, etc.)
    // se "envuelve" el componente actual con el nuevo decorador
    // el precio se va sumando automáticamente
    public void agregarServicioAdicional(CompraComponent decorador) {
        this.componenteActual = decorador;
    }

    // el total real incluye todos los servicios que se hayan agregado
    public double getTotal() {
        return componenteActual.getTotal();
    }

    // útil para mostrarle al usuario qué lleva en su compra
    public String getDescripcionCompleta() {
        return componenteActual.getDescripcion();
    }

    // necesitamos esto para pasarle el componente al Builder
    public CompraComponent componenteActual() {
        return componenteActual;
    }

    // solo cambia el estado, el CompraService es el que decide si se puede pagar
    public void pagar() {
        this.estado = "Pagada";
        System.out.println("Compra pagada. Total: $" + getTotal());
    }

    // getters y setters normales
    public String getIdCompra()            { return idCompra; }
    public void setIdCompra(String id)     { this.idCompra = id; }

    public Usuario getUsuario()            { return usuario; }
    public void setUsuario(Usuario u)      { this.usuario = u; }

    public Evento getEvento()              { return evento; }
    public void setEvento(Evento e)        { this.evento = e; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public double getTotalBase()           { return totalBase; }
    public void setTotalBase(double t)     { this.totalBase = t; }

    public String getEstado()              { return estado; }
    public void setEstado(String estado)   { this.estado = estado; }

    public List<Entrada> getEntradas()     { return entradas; }

    @Override
    public String toString() {
        return "Compra [" + idCompra + " | " +
                (usuario != null ? usuario.getNombreCompleto() : "sin usuario") +
                " | $" + getTotal() + " | " + estado + "]";
    }
}