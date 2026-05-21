package com.Eric.ventaeventos.service;

/*
 * CompraComponent
 *
 * Interfaz base del patrón Decorator.
 *
 * Define los dos métodos que tanto la compra base como todos
 * los decoradores deben tener. Gracias a esto podemos tratar
 * a una compra con VIP + seguro exactamente igual que a una
 * compra sin servicios: ambas tienen getTotal() y getDescripcion().
 *
 * La cadena funciona así:
 * CompraBase → ServicioVIP → SeguroCancelacion
 * cada uno llama al anterior y le suma su propio costo.
 * Al final getTotal() devuelve la suma de todo.
 */
public interface CompraComponent {
    double getTotal();
    String getDescripcion();
}