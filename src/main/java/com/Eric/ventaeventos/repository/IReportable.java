package com.Eric.ventaeventos.service;

/*
 * IReportable
 *
 * Interfaz pequeña y específica para clases que generan reportes.
 * Esto es ISP en práctica: en vez de tener una interfaz gigante
 * con métodos de reporte, notificación y todo mezclado, separamos
 * cada responsabilidad en su propia interfaz.
 * Solo las clases que realmente generan reportes implementan esto.
 */
public interface IReportable {
    String generarResumen();
    String exportarCSV();
}