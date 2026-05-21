package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.Usuario;

/*
 * NotificacionUsuario
 *
 * Implementación concreta del Observer.
 * Cada usuario que compra una entrada se suscribe como observer del evento.
 * Cuando el evento cambia de estado, este objeto recibe la notificación
 * y le avisa al usuario.
 *
 * Por ahora imprime en consola, pero en una versión real aquí iría
 * el envío del correo o la notificación push al celular.
 */
public class NotificacionUsuario implements EventoObserver {

    private final Usuario usuario;

    public NotificacionUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void actualizar(String nombreEvento, String nuevoEstado) {
        System.out.println("Notificacion para " + usuario.getNombreCompleto()
                + ": el evento '" + nombreEvento
                + "' cambio a estado: " + nuevoEstado);
    }
}