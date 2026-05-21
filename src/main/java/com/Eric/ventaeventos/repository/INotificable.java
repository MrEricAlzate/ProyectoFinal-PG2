package com.Eric.ventaeventos.service;

/*
 * INotificable
 *
 * Interfaz para clases que envían notificaciones.
 * Separada de IReportable porque son responsabilidades distintas.
 * Un servicio de reportes no tiene por qué saber notificar,
 * y un servicio de notificaciones no tiene por qué generar reportes.
 */
public interface INotificable {
    void notificar(String mensaje);
    String getTipoNotificacion();
}