package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.*;
import com.Eric.ventaeventos.repository.DataInitializer;

/*
 * CompraFacade
 *
 * Este fue uno de los patrones más útiles del proyecto.
 * El problema era que para hacer una compra desde la pantalla JavaFX
 * había que coordinar como 5 clases distintas: Compra, Pago, Entrada,
 * Asiento y DataInitializer. Eso ponía demasiada lógica en la vista.
 *
 * Con Facade la pantalla solo llama realizarCompra() con los datos
 * del usuario y el evento, y esta clase se encarga de todo lo demás.
 * Si algo cambia en el proceso de compra, solo tocamos esta clase.
 *
 * También maneja la cancelación y registra la incidencia automáticamente.
 */
public class CompraFacade {

    private final DataInitializer data;
    private final CompraService   compraService;
    private static int contadorPago = 1;

    public CompraFacade() {
        this.data         = DataInitializer.getInstance();
        this.compraService = new CompraService();
    }

    /*
     * Punto de entrada principal para comprar entradas.
     * Pasos internos:
     * 1. verifica que el evento esté publicado
     * 2. usa CompraBuilder para armar la compra
     * 3. procesa el pago (simulado)
     * 4. guarda compra y pago en el repositorio
     */
    public Compra realizarCompra(Usuario usuario, Evento evento,
                                 Zona zona, String metodoPago) {

        if (!evento.getEstado().equals("Publicado")) {
            System.out.println("El evento no está disponible.");
            return null;
        }

        Compra compra = new CompraBuilder(usuario, evento)
                .conEntrada(zona)
                .build();

        Pago pago = new Pago(
                "P" + String.format("%03d", contadorPago++),
                metodoPago,
                compra.getTotal(),
                compra);
        pago.procesar();

        data.agregarCompra(compra);
        data.agregarPago(pago);

        System.out.println("Compra finalizada: " + compra.getIdCompra()
                + " | Pago: " + pago.getIdPago());
        return compra;
    }

    /*
     * Cancela una compra y registra la incidencia automáticamente.
     * El cálculo del reembolso lo delega a CompraService que usa Strategy.
     */
    public double cancelarCompra(Compra compra, int diasAntesDelEvento) {
        double reembolso = compraService.cancelarCompra(compra, diasAntesDelEvento);

        // dejamos registro de la cancelación como incidencia
        Incidencia inc = new Incidencia(
                "I" + System.currentTimeMillis(),
                "CANCELACION",
                "Compra cancelada por usuario: " + compra.getIdCompra(),
                "COMPRA",
                compra.getIdCompra());
        data.agregarIncidencia(inc);

        return reembolso;
    }
}