package com.Eric.ventaeventos.service;

/*
 * Comando
 *
 * Interfaz del patrón Command para las acciones del administrador.
 *
 * El problema: cuando el admin publica, pausa o cancela un evento,
 * necesitábamos una forma de registrar esas acciones y poder deshacerlas
 * si se equivocó. Sin Command, tendríamos que agregar lógica de deshacer
 * directamente en cada pantalla, lo cual ensucia mucho el código.
 *
 * Con Command cada acción es un objeto independiente que sabe
 * ejecutarse y deshacerse. La pantalla solo llama ejecutar() o deshacer()
 * sin saber qué hay adentro.
 *
 * Por ahora implementamos PublicarEventoComando, pero se pueden
 * agregar más fácilmente: CancelarEventoComando, PausarEventoComando, etc.
 */
public interface Comando {
    void ejecutar();
    void deshacer();
}