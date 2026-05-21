package com.Eric.ventaeventos.service;

/*
 * SeguroCancelacion
 *
 * Decorador que agrega el seguro de cancelación a una compra.
 * Cuesta $15.000 adicionales y le da al usuario la posibilidad
 * de cancelar con mejores condiciones de reembolso.
 *
 * Al extender ServicioDecorator solo necesitamos definir cuánto suma
 * y qué texto agrega a la descripción, todo lo demás lo hereda.
 */
public class SeguroCancelacion extends ServicioDecorator {

    public SeguroCancelacion(CompraComponent componente) {
        super(componente);
    }

    @Override
    public double getTotal() {
        return super.getTotal() + 15000;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Seguro Cancelación";
    }
}