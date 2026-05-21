package com.Eric.ventaeventos.service.strategy;

/*
 * ReembolsoParcialStrategy
 *
 * Política más estricta, para eventos con alta demanda o donde
 * el organizador tiene costos fijos altos que no puede recuperar
 * si alguien cancela tarde.
 *
 * Las reglas son:
 * - 10 días o más: devuelve el 90%
 * - Entre 5 y 9 días: devuelve el 60%
 * - Menos de 5 días: no hay reembolso
 *
 * El hecho de que sea una clase separada de ReembolsoCompletoStrategy
 * es justo el punto del patrón Strategy: podemos cambiar la política
 * sin modificar CompraService para nada.
 */
public class ReembolsoParcialStrategy implements CancelacionStrategy {

    @Override
    public double calcularReembolso(double montoTotal, int diasAntesDelEvento) {
        if (diasAntesDelEvento >= 10) {
            return montoTotal * 0.90;
        } else if (diasAntesDelEvento >= 5) {
            return montoTotal * 0.60;
        } else {
            return 0.0;  // canceló muy tarde, no hay devolución
        }
    }

    @Override
    public String getDescripcionPolitica() {
        return "Política Estricta: Reembolso limitado";
    }
}