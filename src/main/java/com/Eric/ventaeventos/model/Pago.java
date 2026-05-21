package com.Eric.ventaeventos.model;

import java.time.LocalDateTime;

/*
 * Clase Pago
 *
 * Maneja el pago de una compra. Como no tenemos pasarela de pagos real
 * (Wompi, PayU, etc.) lo simulamos: por ahora siempre aprueba.
 * En un proyecto real aquí iría la conexión con el banco o la pasarela.
 *
 * Métodos de pago que soportamos: Tarjeta, Nequi, PSE, Efectivo.
 * El estado empieza en PENDIENTE y pasa a APROBADO o RECHAZADO
 * dependiendo del resultado del proceso.
 */
public class Pago {

    private String idPago;
    private String metodoPago;
    private double monto;
    private LocalDateTime fechaPago;
    private String estado;   // PENDIENTE, APROBADO, RECHAZADO

    // referencia a la compra que se está pagando
    private Compra compra;

    // todo pago nace pendiente, la fecha se registra automáticamente
    public Pago() {
        this.fechaPago = LocalDateTime.now();
        this.estado    = "PENDIENTE";
    }

    public Pago(String idPago, String metodoPago, double monto, Compra compra) {
        this();
        this.idPago     = idPago;
        this.metodoPago = metodoPago;
        this.monto      = monto;
        this.compra     = compra;
    }

    /*
     * Procesa el pago. Por ahora siempre aprueba porque es simulado.
     * Si fuera real, aquí llamaríamos a la API del banco y dependiendo
     * de la respuesta aprobaríamos o rechazaríamos.
     */
    public boolean procesar() {
        this.estado = "APROBADO";
        this.compra.pagar();
        System.out.println("Pago aprobado: $" + monto + " via " + metodoPago);
        return true;
    }

    public String getIdPago() { return idPago; }
    public void setIdPago(String idPago) { this.idPago = idPago; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    // solo getter porque la fecha no debería cambiar después de creado el pago
    public LocalDateTime getFechaPago() { return fechaPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }

    @Override
    public String toString() {
        return "Pago " + idPago + " — " + metodoPago + " $" + monto + " (" + estado + ")";
    }
}