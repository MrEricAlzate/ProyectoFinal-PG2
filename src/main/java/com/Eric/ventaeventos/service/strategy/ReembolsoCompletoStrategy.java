package com.Eric.ventaeventos.service.strategy;

/*
 * ReembolsoCompletoStrategy
 *
 * Es la política más flexible. La usamos para eventos normales
 * donde el organizador quiere darle confianza al comprador.
 *
 * Las reglas que definimos son:
 * - 7 días o más antes: devuelve el 100%
 * - Entre 3 y 6 días: devuelve el 80%
 * - Menos de 3 días: devuelve el 50%
 *
 * Estos porcentajes los decidimos nosotros, en un caso real
 * los definiría el organizador del evento.
 */
public class ReembolsoCompletoStrategy implements CancelacionStrategy {

    @Override
    public double calcularReembolso(double montoTotal, int diasAntesDelEvento) {
        if (diasAntesDelEvento >= 7) {
            return montoTotal;           // 100%, canceló con suficiente tiempo
        } else if (diasAntesDelEvento >= 3) {
            return montoTotal * 0.80;    // 80%, canceló con poco tiempo
        } else {
            return montoTotal * 0.50;    // 50%, canceló muy tarde
        }
    }

    @Override
    public String getDescripcionPolitica() {
        return "Política Generosa: Reembolso según anticipación";
    }
}