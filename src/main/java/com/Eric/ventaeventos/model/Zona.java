package com.Eric.ventaeventos.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Clase Zona
 *
 * Es un sector dentro del recinto. Por ejemplo el Estadio Centenario
 * tiene zona VIP, Preferencial y General, cada una con su precio y capacidad.
 *
 * Algunas zonas tienen asientos numerados (VIP, Preferencial) y otras no
 * (General de pie). Si la lista de asientos está vacía asumimos que
 * esa zona no maneja numeración específica.
 */
public class Zona {

    private String idZona;
    private String nombre;       // VIP, Preferencial, General, Oriental, etc.
    private int capacidad;
    private double precioBase;

    // solo se llena si la zona tiene asientos numerados
    private List<Asiento> asientos;

    public Zona() {
        this.asientos = new ArrayList<>();
    }

    public Zona(String idZona, String nombre, int capacidad, double precioBase) {
        this();
        this.idZona     = idZona;
        this.nombre     = nombre;
        this.capacidad  = capacidad;
        this.precioBase = precioBase;
    }

    public String getIdZona() { return idZona; }
    public void setIdZona(String idZona) { this.idZona = idZona; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    public List<Asiento> getAsientos() { return asientos; }

    public void agregarAsiento(Asiento asiento) {
        this.asientos.add(asiento);
    }

    /*
     * Calcula cuántos asientos quedan disponibles.
     * Por ahora es una aproximación: capacidad menos asientos registrados.
     * Lo ideal sería filtrar solo los que están en estado "Disponible"
     * pero para este alcance del proyecto funciona así.
     */
    public int getAsientosDisponibles() {
        if (asientos.isEmpty()) {
            return capacidad;
        }
        return (int) asientos.stream()
                .filter(a -> a.getEstado().equals("Disponible"))
                .count();
    }

    @Override
    public String toString() {
        return nombre + " (Capacidad: " + capacidad + ", Precio: $" + precioBase + ")";
    }
}