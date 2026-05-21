package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.Compra;
import java.util.List;

/*
 * ReporteExportador
 *
 * Interfaz del patrón Adapter para exportar reportes.
 *
 * El problema: Apache POI para Excel y FileWriter para CSV tienen
 * APIs completamente distintas. Sin esta interfaz, el código que
 * genera reportes estaría acoplado a una librería específica.
 *
 * Con esta interfaz definimos un contrato común: cualquier exportador
 * debe saber exportar una lista de compras a un archivo y decir
 * qué formato maneja. La pantalla solo conoce esta interfaz,
 * no sabe ni le importa si adentro usa CSV, Excel o PDF.
 */
public interface ReporteExportador {
    void exportar(List<Compra> compras, String rutaArchivo);
    String getFormato();
}