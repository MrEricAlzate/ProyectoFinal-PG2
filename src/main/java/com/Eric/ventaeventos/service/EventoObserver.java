package com.Eric.ventaeventos.service;

/*
 * EventoObserver
 *
 * Interfaz del patrón Observer.
 * Cualquier clase que quiera enterarse cuando un evento cambia de estado
 * solo tiene que implementar este método.
 *
 * Lo usamos para notificar a los usuarios que compraron entradas
 * cuando el evento se cancela o pausa. Así no tenemos que recorrer
 * manualmente la lista de compradores desde Evento.
 */
public interface EventoObserver {
    void actualizar(String nombreEvento, String nuevoEstado);
}