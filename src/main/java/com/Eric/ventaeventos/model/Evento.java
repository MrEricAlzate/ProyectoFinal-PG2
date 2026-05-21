package com.Eric.ventaeventos.model;

import com.Eric.ventaeventos.service.EventoObserver;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Clase Evento
 *
 * Representa cualquier evento de la plataforma: conciertos, teatro, conferencias.
 * Tiene bastantes atributos porque el PDF pedía ciudad, categoría, políticas, etc.
 *
 * Una cosa interesante que implementamos acá es el patrón Observer:
 * cuando el estado del evento cambia (se cancela, se pausa), todos los
 * usuarios suscritos reciben una notificación automáticamente.
 * Eso evita tener que recorrer manualmente la lista de compradores cada vez.
 *
 * Los estados posibles son: Borrador, Publicado, Pausado, Cancelado, Finalizado.
 * Un evento nuevo siempre empieza en Borrador hasta que el admin lo publique.
 */
public class Evento {

    private String idEvento;
    private String nombre;
    private String categoria;       // Concierto, Teatro, Conferencia
    private String descripcion;
    private String ciudad;
    private LocalDateTime fechaHora;
    private String estado;

    private Recinto recinto;        // dónde se hace el evento

    // lista de observers, se llenan cuando los usuarios compran entradas
    private List<EventoObserver> observers = new ArrayList<>();

    // políticas que define el admin al crear el evento
    private String politicaCancelacion;
    private String politicaReembolso;

    // todo evento nace como Borrador, el admin decide cuándo publicarlo
    public Evento() {
        this.estado = "Borrador";
    }

    public Evento(String idEvento, String nombre, String categoria,
                  String ciudad, LocalDateTime fechaHora, Recinto recinto) {
        this();
        this.idEvento  = idEvento;
        this.nombre    = nombre;
        this.categoria = categoria;
        this.ciudad    = ciudad;
        this.fechaHora = fechaHora;
        this.recinto   = recinto;
    }

    // métodos del Observer, cualquier clase que implemente EventoObserver
    // puede suscribirse para recibir notificaciones de cambios
    public void agregarObserver(EventoObserver observer) {
        observers.add(observer);
    }

    public void removerObserver(EventoObserver observer) {
        observers.remove(observer);
    }

    // este es privado porque solo se llama internamente cuando cambia el estado
    private void notificarObservers() {
        for (EventoObserver o : observers) {
            o.actualizar(this.nombre, this.estado);
        }
    }

    // getters y setters
    public String getIdEvento() { return idEvento; }
    public void setIdEvento(String idEvento) { this.idEvento = idEvento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Recinto getRecinto() { return recinto; }
    public void setRecinto(Recinto recinto) { this.recinto = recinto; }

    public String getPoliticaCancelacion() { return politicaCancelacion; }
    public void setPoliticaCancelacion(String p) { this.politicaCancelacion = p; }

    public String getPoliticaReembolso() { return politicaReembolso; }
    public void setPoliticaReembolso(String p) { this.politicaReembolso = p; }

    // cada vez que cambia el estado notificamos a los suscritos
    public void publicar() {
        this.estado = "Publicado";
        System.out.println("Evento publicado: " + nombre);
        notificarObservers();
    }

    public void pausar() {
        this.estado = "Pausado";
        System.out.println("Evento pausado: " + nombre);
        notificarObservers();
    }

    public void cancelar() {
        this.estado = "Cancelado";
        System.out.println("Evento cancelado: " + nombre);
        notificarObservers();
    }

    @Override
    public String toString() {
        return "Evento: " + nombre + " - " + fechaHora + " (" + estado + ")";
    }
}