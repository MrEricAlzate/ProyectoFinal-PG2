package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.*;

/*
 * CompraBuilder
 *
 * Usamos Builder porque construir una Compra tiene muchos pasos opcionales:
 * puede llevar una o varias entradas, puede tener VIP, seguro, asiento
 * numerado o no. Con un constructor normal tendríamos que pasar todos
 * esos parámetros de una vez y se volvía muy difícil de leer.
 *
 * Con Builder armamos la compra paso a paso y al final llamamos build()
 * que devuelve el objeto listo. Cada método retorna "this" para poder
 * encadenar las llamadas en una sola línea si queremos.
 *
 * Ejemplo de uso:
 * Compra c = new CompraBuilder(usuario, evento)
 *                 .conEntrada(zonaVIP)
 *                 .conSeguroCancelacion()
 *                 .build();
 */
public class CompraBuilder {

    private final Compra compra;

    // contador estático para generar IDs únicos por cada compra creada
    private static int contador = 1;

    public CompraBuilder(Usuario usuario, Evento evento) {
        this.compra = new Compra(usuario, evento);
        this.compra.setIdCompra("C" + String.format("%03d", contador++));
    }

    // agrega una entrada a la zona sin asiento específico (ej: General)
    public CompraBuilder conEntrada(Zona zona) {
        Entrada entrada = new Entrada(zona, zona.getPrecioBase());
        entrada.setIdEntrada("ENT" + (compra.getEntradas().size() + 1));
        compra.getEntradas().add(entrada);
        compra.setTotalBase(compra.getTotalBase() + zona.getPrecioBase());
        return this;
    }

    // agrega una entrada con asiento numerado (ej: VIP fila A asiento 3)
    // también marca el asiento como reservado para que nadie más lo tome
    public CompraBuilder conAsiento(Zona zona, Asiento asiento) {
        Entrada entrada = new Entrada(zona, zona.getPrecioBase());
        entrada.setAsiento(asiento);
        entrada.setIdEntrada("ENT" + (compra.getEntradas().size() + 1));
        compra.getEntradas().add(entrada);
        compra.setTotalBase(compra.getTotalBase() + zona.getPrecioBase());
        asiento.reservar();
        return this;
    }

    // envuelve la compra con el decorador VIP, suma $50.000
    public CompraBuilder conServicioVIP() {
        compra.agregarServicioAdicional(
                new ServicioVIP(compra.componenteActual()));
        return this;
    }

    // envuelve la compra con el decorador de seguro, suma $15.000
    public CompraBuilder conSeguroCancelacion() {
        compra.agregarServicioAdicional(
                new SeguroCancelacion(compra.componenteActual()));
        return this;
    }

    // último paso: valida que todo esté bien y devuelve la compra lista
    public Compra build() {
        System.out.println("Compra creada: " + compra.getIdCompra()
                + " | " + compra.getDescripcionCompleta()
                + " | $" + compra.getTotal());
        return compra;
    }
}