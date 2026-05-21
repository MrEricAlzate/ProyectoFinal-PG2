package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.Evento;
import com.Eric.ventaeventos.model.Recinto;
import java.time.LocalDateTime;

/*
 * EventoFactory
 *
 * Patrón Factory: centraliza la creación de eventos según su tipo.
 *
 * El problema que resolvimos: cada vez que creábamos un evento en el código
 * teníamos que acordarnos de setearle la descripción, las políticas y la
 * categoría correcta. Si se nos olvidaba algo quedaba incompleto.
 *
 * Con Factory cada tipo de evento tiene su propio método que ya sabe
 * qué valores por defecto ponerle. El admin solo pasa los datos
 * específicos (nombre, ciudad, fecha) y la fábrica hace el resto.
 *
 * Si en el futuro agregamos un nuevo tipo (Festival, Deportivo, etc.)
 * solo agregamos un método acá sin tocar nada más.
 */
public class EventoFactory {

    // crea un concierto con sus políticas típicas
    public static Evento crearConcierto(String id, String nombre,
                                        String ciudad, LocalDateTime fecha,
                                        Recinto recinto) {
        Evento e = new Evento(id, nombre, "Concierto", ciudad, fecha, recinto);
        e.setDescripcion("Evento musical en vivo.");
        e.setPoliticaCancelacion("Reembolso 100% con más de 7 días.");
        e.setPoliticaReembolso("Reembolso parcial entre 3 y 7 días.");
        return e;
    }

    // el teatro tiene políticas más estrictas porque los costos de producción
    // son más altos y no se pueden recuperar fácilmente
    public static Evento crearTeatro(String id, String nombre,
                                     String ciudad, LocalDateTime fecha,
                                     Recinto recinto) {
        Evento e = new Evento(id, nombre, "Teatro", ciudad, fecha, recinto);
        e.setDescripcion("Obra de teatro en vivo.");
        e.setPoliticaCancelacion("Sin reembolso con menos de 3 días.");
        e.setPoliticaReembolso("Reembolso 80% con más de 5 días.");
        return e;
    }

    // las conferencias suelen ser más flexibles porque se pueden revender cupos
    public static Evento crearConferencia(String id, String nombre,
                                          String ciudad, LocalDateTime fecha,
                                          Recinto recinto) {
        Evento e = new Evento(id, nombre, "Conferencia", ciudad, fecha, recinto);
        e.setDescripcion("Conferencia académica o empresarial.");
        e.setPoliticaCancelacion("Reembolso completo hasta 24h antes.");
        e.setPoliticaReembolso("Sin reembolso el día del evento.");
        return e;
    }
}