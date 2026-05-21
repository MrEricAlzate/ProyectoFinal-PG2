package com.Eric.ventaeventos.model;

/*
 * Clase Asiento
 *
 * Cada asiento es una silla numerada dentro de una zona.
 * No todas las zonas tienen asientos numerados, por ejemplo
 * la zona General puede ser de pie, pero VIP y Preferencial sí los tienen.
 *
 * El estado del asiento cambia según lo que pase con la compra:
 * - Disponible: nadie lo tiene
 * - Reservado: alguien lo está comprando pero no ha pagado
 * - Vendido: ya fue pagado
 * - Bloqueado: el admin lo bloqueó (mantenimiento, cortesías, etc.)
 */
public class Asiento {

    private String idAsiento;
    private String fila;      // letra, ej: A, B, C
    private String numero;    // número dentro de la fila, ej: 1, 2, 3
    private String estado;

    // por defecto todo asiento nace disponible
    public Asiento() {
        this.estado = "Disponible";
    }

    public Asiento(String idAsiento, String fila, String numero) {
        this();
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
    }

    public String getIdAsiento() { return idAsiento; }
    public void setIdAsiento(String idAsiento) { this.idAsiento = idAsiento; }

    public String getFila() { return fila; }
    public void setFila(String fila) { this.fila = fila; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // estos métodos los usamos cuando el usuario selecciona o compra un asiento
    // reservar() se llama cuando está en proceso de pago (no confirmado aún)
    public void reservar() {
        this.estado = "Reservado";
    }

    // vender() se llama cuando el pago fue aprobado
    public void vender() {
        this.estado = "Vendido";
    }

    // liberar() sirve si la compra se cancela antes de pagar
    public void liberar() {
        this.estado = "Disponible";
    }

    @Override
    public String toString() {
        return "Asiento " + fila + numero + " (" + estado + ")";
    }
}