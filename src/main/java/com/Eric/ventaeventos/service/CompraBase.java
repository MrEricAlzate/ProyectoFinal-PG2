package com.Eric.ventaeventos.service;

import com.Eric.ventaeventos.model.Compra;

/*
 * CompraBase
 *
 * Es el componente base del patrón Decorator.
 * Representa una compra sin ningún servicio adicional todavía.
 *
 * El Decorator funciona así: CompraBase envuelve a Compra y devuelve
 * el precio base. Cuando el usuario agrega VIP o seguro, esos decoradores
 * envuelven a CompraBase y suman su costo encima.
 *
 * Un error que tuvimos al principio fue llamar getTotal() dentro de getTotal()
 * lo que causaba un ciclo infinito. Lo corregimos usando getTotalBase()
 * que devuelve el precio sin pasar por el Decorator.
 */
public class CompraBase implements CompraComponent {

    private Compra compra;

    public CompraBase(Compra compra) {
        this.compra = compra;
    }

    @Override
    public double getTotal() {
        return compra.getTotalBase();
    }

    @Override
    public String getDescripcion() {
        return "Compra base";
    }
}