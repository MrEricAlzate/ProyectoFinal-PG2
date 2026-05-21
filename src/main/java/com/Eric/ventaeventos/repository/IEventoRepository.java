package com.Eric.ventaeventos.repository;

import com.Eric.ventaeventos.model.Evento;
import java.util.List;

/*
 * IEventoRepository
 *
 * Interfaz que define qué operaciones se pueden hacer con eventos.
 * Aplicamos DIP: los servicios dependen de esta interfaz, no de
 * DataInitializer directamente. Si después conectamos una base de datos
 * real, solo creamos otra implementación de esta interfaz.
 */
public interface IEventoRepository {
    List<Evento> obtenerEventos();
    Evento buscarEventoPorId(String id);
    void agregar(Evento evento);
}