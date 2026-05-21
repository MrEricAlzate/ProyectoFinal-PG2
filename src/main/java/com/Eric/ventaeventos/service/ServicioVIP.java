package com.Eric.ventaeventos.service;

/*
 * ServicioVIP
 *
 * Decorador que agrega acceso VIP a una compra.
 * Suma $50.000 al total e incluye beneficios como zona preferencial,
 * acceso anticipado y atención personalizada (en un caso real).
 *
 * Se puede combinar con otros decoradores, por ejemplo:
 * CompraBase → ServicioVIP → SeguroCancelacion
 * el orden no afecta el total pero sí la descripción.
 */
public class ServicioVIP extends ServicioDecorator {

    public ServicioVIP(CompraComponent componente) {
        super(componente);
    }

    @Override
    public double getTotal() {
        return super.getTotal() + 50000;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + VIP";
    }
}