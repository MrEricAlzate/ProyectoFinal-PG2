package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.Compra;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 * ReportePDFAdapter
 *
 * Adaptador concreto que exporta compras a PDF usando PDFBox.
 * Implementa la misma interfaz que ReporteCSVAdapter, así la pantalla
 * puede cambiar entre CSV y PDF sin modificar nada más.
 *
 * Usamos PDFBox porque es la librería que pide el PDF del proyecto
 * y porque es gratuita y fácil de usar para reportes simples.
 */
public class ReportePDFAdapter implements ReporteExportador {

    @Override
    public void exportar(List<Compra> compras, String rutaArchivo) {

        // creamos el documento PDF
        try (PDDocument documento = new PDDocument()) {

            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);

            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

            // fuentes que vamos a usar
            PDType1Font fuenteTitulo  = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font fuenteNormal  = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fuentePequena = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);

            float margen    = 50;
            float anchoUtil = PDRectangle.A4.getWidth() - 2 * margen;
            float y         = PDRectangle.A4.getHeight() - margen;

            // título del reporte
            contenido.beginText();
            contenido.setFont(fuenteTitulo, 18);
            contenido.newLineAtOffset(margen, y);
            contenido.showText("Reporte de Compras");
            contenido.endText();
            y -= 25;

            // subtítulo con fecha de generación
            String fecha = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            contenido.beginText();
            contenido.setFont(fuentePequena, 10);
            contenido.newLineAtOffset(margen, y);
            contenido.showText("Generado el: " + fecha);
            contenido.endText();
            y -= 10;

            // línea separadora
            contenido.moveTo(margen, y);
            contenido.lineTo(margen + anchoUtil, y);
            contenido.stroke();
            y -= 20;

            // encabezados de la tabla
            contenido.beginText();
            contenido.setFont(fuenteTitulo, 11);
            contenido.newLineAtOffset(margen, y);
            contenido.showText("ID");
            contenido.newLineAtOffset(80, 0);
            contenido.showText("Usuario");
            contenido.newLineAtOffset(150, 0);
            contenido.showText("Evento");
            contenido.newLineAtOffset(150, 0);
            contenido.showText("Total");
            contenido.newLineAtOffset(80, 0);
            contenido.showText("Estado");
            contenido.endText();
            y -= 8;

            // línea debajo de encabezados
            contenido.moveTo(margen, y);
            contenido.lineTo(margen + anchoUtil, y);
            contenido.stroke();
            y -= 18;

            // filas de datos
            for (Compra c : compras) {

                // si ya no cabe más en la página, creamos una nueva
                if (y < margen + 40) {
                    contenido.close();
                    PDPage paginaNueva = new PDPage(PDRectangle.A4);
                    documento.addPage(paginaNueva);
                    contenido = new PDPageContentStream(documento, paginaNueva);
                    y = PDRectangle.A4.getHeight() - margen;
                }

                String idCompra  = c.getIdCompra()  != null ? c.getIdCompra()  : "N/A";
                String usuario   = c.getUsuario()   != null ? c.getUsuario().getNombreCompleto() : "N/A";
                String evento    = c.getEvento()    != null ? c.getEvento().getNombre()          : "N/A";
                String total     = "$" + String.format("%.0f", c.getTotal());
                String estado    = c.getEstado()    != null ? c.getEstado()    : "N/A";

                // truncamos textos largos para que no se salgan de la columna
                if (usuario.length() > 18) usuario = usuario.substring(0, 18) + "...";
                if (evento.length()  > 18) evento  = evento.substring(0, 18)  + "...";

                contenido.beginText();
                contenido.setFont(fuenteNormal, 10);
                contenido.newLineAtOffset(margen, y);
                contenido.showText(idCompra);
                contenido.newLineAtOffset(80, 0);
                contenido.showText(usuario);
                contenido.newLineAtOffset(150, 0);
                contenido.showText(evento);
                contenido.newLineAtOffset(150, 0);
                contenido.showText(total);
                contenido.newLineAtOffset(80, 0);
                contenido.showText(estado);
                contenido.endText();
                y -= 18;
            }

            // línea final y total de registros
            y -= 5;
            contenido.moveTo(margen, y);
            contenido.lineTo(margen + anchoUtil, y);
            contenido.stroke();
            y -= 15;

            contenido.beginText();
            contenido.setFont(fuentePequena, 10);
            contenido.newLineAtOffset(margen, y);
            contenido.showText("Total de compras: " + compras.size());
            contenido.endText();

            contenido.close();
            documento.save(rutaArchivo);
            System.out.println("PDF exportado: " + rutaArchivo);

        } catch (IOException e) {
            System.out.println("Error exportando PDF: " + e.getMessage());
        }
    }

    @Override
    public String getFormato() { return "PDF"; }
}