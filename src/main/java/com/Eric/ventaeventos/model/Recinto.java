package com.Eric.ventaeventos.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Clase Recinto
 *
 * Es el lugar físico donde ocurre el evento: un estadio, un teatro,
 * un auditorio, etc. Un recinto puede usarse para varios eventos distintos.
 *
 * Cada recinto tiene zonas (VIP, Preferencial, General) y cada zona
 * puede tener sus propios asientos numerados o simplemente una capacidad.
 * Esa relación la manejamos con la lista de zonas acá adentro.
 */
public class Recinto {

    private String idRecinto;
    private String nombre;
    private String direccion;
    private String ciudad;

    // un recinto agrupa varias zonas, por eso es una lista
    private List<Zona> zonas;

    public Recinto() {
        this.zonas = new ArrayList<>();
    }

    public Recinto(String idRecinto, String nombre, String direccion, String ciudad) {
        this();
        this.idRecinto = idRecinto;
        this.nombre    = nombre;
        this.direccion = direccion;
        this.ciudad    = ciudad;
    }

    public String getIdRecinto() { return idRecinto; }
    public void setIdRecinto(String idRecinto) { this.idRecinto = idRecinto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public List<Zona> getZonas() { return zonas; }

    // el admin agrega las zonas cuando configura el recinto
    public void agregarZona(Zona zona) {
        this.zonas.add(zona);
    }

    @Override
    public String toString() {
        return "Recinto: " + nombre + " - " + ciudad;
    }
}