package com.Eric.ventaeventos.service.strategy;

/*
 * CancelacionStrategy
 *
 * Interfaz del patrón Strategy para manejar las políticas de reembolso.
 *
 * El problema que resuelve: cada evento puede tener reglas distintas
 * para devolver el dinero cuando alguien cancela. Un concierto puede
 * devolver el 100% si cancelas con 7 días, un teatro quizás solo el 50%.
 *
 * Sin Strategy tendríamos un if/else gigante en CompraService que habría
 * que modificar cada vez que se agregue una política nueva, violando OCP.
 * Con Strategy cada política es una clase separada y se puede cambiar
 * en tiempo de ejecución sin tocar el código existente.
 *
 * Implementaciones actuales:
 * - ReembolsoCompletoStrategy: política generosa, devuelve más
 * - ReembolsoParcialStrategy: política estricta, devuelve menos
 */
public interface CancelacionStrategy {

    // recibe el monto total y con cuántos días de anticipación se cancela
    // devuelve cuánto se le regresa al usuario
    double calcularReembolso(double montoTotal, int diasAntesDelEvento);

    // descripción legible para mostrarle al usuario qué política se aplicó
    String getDescripcionPolitica();
}