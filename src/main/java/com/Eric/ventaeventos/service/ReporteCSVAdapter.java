package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.Compra;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*
 * ReporteCSVAdapter
 *
 * Adaptador concreto que exporta compras a un archivo CSV.
 * Implementa ReporteExportador para que se pueda usar de forma
 * intercambiable con otros exportadores (PDF, Excel).
 *
 * El archivo queda en la raíz del proyecto con el nombre que se le pase.
 * Las columnas son: ID, Usuario, Evento, Total, Estado.
 *
 * Si en el futuro queremos exportar a Excel, creamos ReporteExcelAdapter
 * implementando la misma interfaz, sin tocar nada más.
 */
public class ReporteCSVAdapter implements ReporteExportador {

    @Override
    public void exportar(List<Compra> compras, String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {

            writer.write("ID,Usuario,Evento,Total,Estado\n");

            for (Compra c : compras) {
                writer.write(
                        c.getIdCompra() + "," +
                                (c.getUsuario() != null ? c.getUsuario().getNombreCompleto() : "N/A") + "," +
                                (c.getEvento()  != null ? c.getEvento().getNombre()          : "N/A") + "," +
                                c.getTotal()    + "," +
                                c.getEstado()   + "\n"
                );
            }
            System.out.println("CSV exportado: " + rutaArchivo);

        } catch (IOException e) {
            System.out.println("Error exportando CSV: " + e.getMessage());
        }
    }

    @Override
    public String getFormato() { return "CSV"; }
}