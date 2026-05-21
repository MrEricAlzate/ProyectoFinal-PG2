package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.Evento;

/*
 * PublicarEventoComando
 *
 * Implementación concreta del patrón Command para publicar eventos.
 *
 * Lo importante acá es que guardamos el estado anterior antes de ejecutar.
 * Eso nos permite deshacer la acción si el admin se equivocó.
 * Por ejemplo si publicó un evento que todavía no estaba listo,
 * puede llamar deshacer() y el evento vuelve a Borrador.
 *
 * Se pueden crear más comandos siguiendo este mismo esquema:
 * CancelarEventoComando, PausarEventoComando, etc.
 */
public class PublicarEventoComando implements Comando {

    private final Evento evento;
    private String estadoAnterior;  // guardamos esto para poder deshacer

    public PublicarEventoComando(Evento evento) {
        this.evento = evento;
    }

    @Override
    public void ejecutar() {
        this.estadoAnterior = evento.getEstado();
        evento.publicar();
    }

    @Override
    public void deshacer() {
        evento.setEstado(estadoAnterior);
        System.out.println("Deshacer: evento '" + evento.getNombre()
                + "' volvio a: " + estadoAnterior);
    }
}