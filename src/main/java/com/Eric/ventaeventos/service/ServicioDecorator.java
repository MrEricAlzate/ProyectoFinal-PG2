package com.Eric.ventaeventos.service;

/*
 * ServicioDecorator
 *
 * Clase abstracta que es el corazón del patrón Decorator.
 * Envuelve cualquier CompraComponent y delega las llamadas
 * al componente interior. Las subclases solo sobreescriben
 * lo que necesitan cambiar (sumar su costo, agregar su descripción).
 *
 * La idea es que se pueden encadenar decoradores:
 * CompraBase → SeguroCancelacion → ServicioVIP
 * cada uno llama al anterior con super.getTotal() y le suma lo suyo.
 */
public abstract class ServicioDecorator implements CompraComponent {

    // el componente que estamos decorando, puede ser CompraBase u otro decorador
    protected CompraComponent componente;

    public ServicioDecorator(CompraComponent componente) {
        this.componente = componente;
    }

    // por defecto delegamos al componente interior
    // las subclases llaman super.getTotal() y le suman su precio
    @Override
    public double getTotal() {
        return componente.getTotal();
    }

    @Override
    public String getDescripcion() {
        return componente.getDescripcion();
    }
}