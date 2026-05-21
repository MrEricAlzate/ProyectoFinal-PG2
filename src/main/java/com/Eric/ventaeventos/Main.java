package com.Eric.ventaeventos;

import com.Eric.ventaeventos.repository.DataInitializer;
import com.Eric.ventaeventos.service.*;
import com.Eric.ventaeventos.model.*;
import com.Eric.ventaeventos.service.strategy.ReembolsoParcialStrategy;

import java.time.LocalDateTime;

/*
 * Main
 *
 * Clase principal del proyecto. Por ahora sirve para probar que todos
 * los patrones y la lógica de negocio funcionan correctamente antes
 * de conectarlos con las pantallas JavaFX.
 *
 * Cada sección prueba un patrón distinto y muestra el resultado en consola.
 * Cuando JavaFX esté listo, esta clase se reemplaza por la pantalla de inicio.
 */
public class Main {
    public static void main(String[] args) {

        // el Singleton carga todos los datos de prueba la primera vez
        DataInitializer data = DataInitializer.getInstance();

        System.out.println("=== USUARIOS ===");
        data.obtenerUsuarios().forEach(System.out::println);

        System.out.println("\n=== EVENTOS ===");
        data.obtenerEventos().forEach(System.out::println);

        // verificamos que el Singleton devuelve siempre la misma instancia
        System.out.println("\nSingleton funciona: " + (data == DataInitializer.getInstance()));

        // ── Decorator ───────────────────────────────────────────────
        // mostramos cómo el precio base se va incrementando
        // a medida que se agregan servicios adicionales
        System.out.println("\n=== DECORATOR ===");
        Usuario eric   = data.buscarUsuarioPorId("U001");
        Evento concert = data.buscarEventoPorId("E001");

        Compra compra = new Compra(eric, concert);
        compra.setIdCompra("C001");
        compra.setTotalBase(180000);

        System.out.println("Sin servicios:  " + compra.getDescripcionCompleta()
                + " → $" + compra.getTotal());

        compra.agregarServicioAdicional(new ServicioVIP(compra.componenteActual()));
        System.out.println("Con VIP:        " + compra.getDescripcionCompleta()
                + " → $" + compra.getTotal());

        // ── Strategy ────────────────────────────────────────────────
        // cancelamos con política estricta para ver que el reembolso
        // es menor que con la política generosa
        System.out.println("\n=== STRATEGY ===");
        compra.pagar();
        CompraService servicio = new CompraService();
        servicio.setCancelacionStrategy(new ReembolsoParcialStrategy());
        double reembolso = servicio.cancelarCompra(compra, 3);
        System.out.println("Reembolso recibido: $" + reembolso);

        // ── Factory ─────────────────────────────────────────────────
        // creamos una conferencia, la fábrica le asigna las políticas
        // correctas según el tipo sin que tengamos que hacerlo manualmente
        System.out.println("\n=== FACTORY ===");
        Recinto recinto = data.getRecintos().get(0);
        Evento conferencia = EventoFactory.crearConferencia(
                "E003", "JavaConf 2026", "Medellín",
                LocalDateTime.of(2026, 10, 20, 9, 0), recinto);
        conferencia.publicar();
        System.out.println(conferencia);
        System.out.println("Politica: " + conferencia.getPoliticaCancelacion());

        // ── Builder ─────────────────────────────────────────────────
        // armamos la compra paso a paso, se nota que es más legible
        // que pasar todos los parámetros en un solo constructor
        System.out.println("\n=== BUILDER ===");
        Usuario maria = data.buscarUsuarioPorId("U002");
        Zona zonaVIP  = data.getRecintos().get(0).getZonas().get(0);

        Compra compraBuilder = new CompraBuilder(maria, conferencia)
                .conEntrada(zonaVIP)
                .conSeguroCancelacion()
                .build();

        System.out.println("Estado: "   + compraBuilder.getEstado());
        System.out.println("Entradas: " + compraBuilder.getEntradas().size());

        // ── Facade ──────────────────────────────────────────────────
        // una sola llamada hace todo: crea compra, procesa pago y guarda
        System.out.println("\n=== FACADE ===");
        CompraFacade facade   = new CompraFacade();
        Zona zonaGeneral      = data.getRecintos().get(0).getZonas().get(2);
        Compra compraFacade   = facade.realizarCompra(eric, concert, zonaGeneral, "Nequi");
        System.out.println("Compras en sistema: " + data.getCompras().size());

        // ── Adapter ─────────────────────────────────────────────────
        // exportamos usando la interfaz, no importa si adentro es CSV o PDF
        System.out.println("\n=== ADAPTER ===");
        ReporteExportador exportador = new ReporteCSVAdapter();
        exportador.exportar(data.getCompras(), "reporte_compras.csv");
        System.out.println("Formato: " + exportador.getFormato());

        // ── Observer ────────────────────────────────────────────────
        // suscribimos a eric y maria al concierto
        // cuando se cancela ambos reciben la notificación automáticamente
        System.out.println("\n=== OBSERVER ===");
        NotificacionUsuario obsEric  = new NotificacionUsuario(eric);
        NotificacionUsuario obsMaria = new NotificacionUsuario(maria);
        concert.agregarObserver(obsEric);
        concert.agregarObserver(obsMaria);
        concert.cancelar();

        // ── Command ─────────────────────────────────────────────────
        // publicamos un evento y luego deshacemos la acción
        // el evento vuelve al estado que tenía antes
        System.out.println("\n=== COMMAND ===");
        Evento eventoNuevo = EventoFactory.crearConcierto("E004", "Festival Rock",
                "Cali", LocalDateTime.of(2026, 11, 1, 18, 0), recinto);
        Comando cmd = new PublicarEventoComando(eventoNuevo);
        cmd.ejecutar();
        cmd.deshacer();

        // ── Reporte PDF ─────────────────────────────────────────────────
        System.out.println("\n=== REPORTE PDF ===");
        ReporteExportador exportadorPDF = new ReportePDFAdapter();
        exportadorPDF.exportar(data.getCompras(), "reporte_compras.pdf");
        System.out.println("Formato: " + exportadorPDF.getFormato());
    }
}