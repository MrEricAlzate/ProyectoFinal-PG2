package com.Eric.ventaeventos.model;

/*
 * Clase Entrada
 *
 * Es el ticket en sí. Cuando una compra se paga, se generan
 * las entradas correspondientes, una por cada asiento o cupo seleccionado.
 *
 * El asiento puede ser null porque no todas las zonas son numeradas,
 * en zona General por ejemplo solo importa la zona, no el asiento específico.
 *
 * Estados posibles:
 * - Activa: entrada válida, el usuario puede entrar al evento
 * - Usada: ya pasó por la puerta
 * - Anulada: se canceló la compra o hubo un reembolso
 */
public class Entrada {

    private String idEntrada;
    private Zona zona;
    private Asiento asiento;   // null si la zona no maneja asientos numerados
    private double precioFinal;
    private String estado;

    public Entrada() {
        this.estado = "Activa";
    }

    // este es el constructor que más usamos al generar entradas
    public Entrada(Zona zona, double precioFinal) {
        this();
        this.zona = zona;
        this.precioFinal = precioFinal;
    }

    public String getIdEntrada() { return idEntrada; }
    public void setIdEntrada(String idEntrada) { this.idEntrada = idEntrada; }

    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }

    // si el asiento es null simplemente no se muestra en el toString
    public Asiento getAsiento() { return asiento; }
    public void setAsiento(Asiento asiento) { this.asiento = asiento; }

    public double getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(double precioFinal) { this.precioFinal = precioFinal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        String info = "Entrada " + idEntrada + " - " +
                (zona != null ? zona.getNombre() : "sin zona") +
                " ($" + precioFinal + ")";
        // si tiene asiento asignado lo mostramos también
        if (asiento != null) {
            info += " | " + asiento.toString();
        }
        return info;
    }
}