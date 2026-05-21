package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.Compra;
import com.Eric.ventaeventos.service.strategy.CancelacionStrategy;
import com.Eric.ventaeventos.service.strategy.ReembolsoCompletoStrategy;

/*
 * CompraService
 *
 * Tiene la lógica de negocio relacionada con compras.
 * La separamos de la clase Compra para cumplir SRP: Compra solo
 * guarda datos, CompraService decide qué se puede hacer con esos datos.
 *
 * Además combina dos patrones:
 * - SRP: la lógica de negocio no está mezclada con el modelo
 * - Strategy: la política de reembolso se puede cambiar en cualquier momento
 *   sin modificar este servicio (cumple OCP también)
 *
 * Por defecto usa ReembolsoCompletoStrategy pero se puede cambiar
 * antes de cancelar si el evento tiene una política distinta.
 */
public class CompraService {

    private CancelacionStrategy cancelacionStrategy;

    // por defecto usamos la política generosa
    public CompraService() {
        this.cancelacionStrategy = new ReembolsoCompletoStrategy();
    }

    // permite cambiar la política antes de cancelar
    public void setCancelacionStrategy(CancelacionStrategy strategy) {
        this.cancelacionStrategy = strategy;
    }

    public double cancelarCompra(Compra compra, int diasAntesDelEvento) {
        double reembolso = cancelacionStrategy.calcularReembolso(
                compra.getTotal(), diasAntesDelEvento);
        compra.setEstado("Cancelada");
        System.out.println("Compra cancelada. Reembolso: $" + reembolso
                + " — " + cancelacionStrategy.getDescripcionPolitica());
        return reembolso;
    }

    // solo confirma si el pago ya fue procesado
    public void confirmarCompra(Compra compra) {
        if (compra.getEstado().equals("Pagada")) {
            compra.setEstado("Confirmada");
            System.out.println("Compra confirmada: " + compra.getIdCompra());
        }
    }
}