package com.Eric.ventaeventos.repository;

import com.Eric.ventaeventos.model.*;
import com.Eric.ventaeventos.service.EventoFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * DataInitializer
 *
 * Esta clase hace dos cosas importantes:
 *
 * 1. Patrón Singleton: garantiza que solo exista una instancia en toda
 *    la aplicación. Así todos los módulos (pantallas, servicios) acceden
 *    a los mismos datos sin crear copias distintas.
 *
 * 2. Principio DIP: en vez de que los servicios dependan directamente
 *    de esta clase, implementamos las interfaces IUsuarioRepository e
 *    IEventoRepository. Si en el futuro queremos conectar una base de datos
 *    real, solo cambiamos la implementación sin tocar el resto del código.
 *
 * Los datos que carga son de prueba para poder demostrar el funcionamiento
 * sin necesidad de un formulario de registro.
 */
public class DataInitializer implements IUsuarioRepository, IEventoRepository {

    // la instancia estática es la clave del Singleton
    private static DataInitializer instance;

    // todas las listas son final porque no queremos que se reemplacen,
    // solo que se llenen con datos
    private final List<Usuario>    usuarios    = new ArrayList<>();
    private final List<Evento>     eventos     = new ArrayList<>();
    private final List<Recinto>    recintos    = new ArrayList<>();
    private final List<Compra>     compras     = new ArrayList<>();
    private final List<Incidencia> incidencias = new ArrayList<>();
    private final List<Pago>       pagos       = new ArrayList<>();

    // privado para que nadie pueda hacer new DataInitializer() desde afuera
    private DataInitializer() {
        cargarDatos();
    }

    // único punto de acceso, si no existe la crea, si ya existe la devuelve
    public static DataInitializer getInstance() {
        if (instance == null) {
            instance = new DataInitializer();
        }
        return instance;
    }

    /*
     * Carga los datos iniciales de prueba.
     * Creamos usuarios, un recinto con sus zonas y asientos,
     * y dos eventos publicados para que la app tenga algo que mostrar.
     */
    private void cargarDatos() {

        // usuarios de prueba con sus métodos de pago
        Usuario u1 = new Usuario("U001", "Eric Santiago",
                "eric@gmail.com", "3001234567");
        u1.agregarMetodoDePago("Tarjeta Débito");
        u1.agregarMetodoDePago("Nequi");

        Usuario u2 = new Usuario("U002", "Maria Lopez",
                "maria@gmail.com", "3109876543");
        u2.agregarMetodoDePago("Tarjeta Crédito");

        usuarios.add(u1);
        usuarios.add(u2);

        // recinto con tres zonas: VIP, Preferencial y General
        Recinto estadio = new Recinto("R001", "Estadio Centenario",
                "Av. Siempre Viva 123", "Armenia");

        Zona vip          = new Zona("Z001", "VIP",         36,  350000);
        Zona preferencial = new Zona("Z002", "Preferencial", 72,  180000);
        Zona general      = new Zona("Z003", "General",     200,   80000);

        // la zona VIP tiene asientos numerados, fila A del 1 al 6
        for (int i = 1; i <= 6; i++) {
            vip.agregarAsiento(new Asiento("A" + i + "-VIP", "A", String.valueOf(i)));
        }

        estadio.agregarZona(vip);
        estadio.agregarZona(preferencial);
        estadio.agregarZona(general);
        recintos.add(estadio);

        // dos eventos publicados para mostrar en la pantalla principal
        Evento concierto = new Evento("E001", "Concierto Juanes",
                "Concierto", "Armenia",
                LocalDateTime.of(2026, 8, 15, 20, 0), estadio);
        concierto.setDescripcion("El mejor concierto del año en Armenia.");
        concierto.setPoliticaCancelacion("Reembolso 100% con más de 7 días.");
        concierto.publicar();

        Evento teatro = new Evento("E002", "Romeo y Julieta",
                "Teatro", "Bogotá",
                LocalDateTime.of(2026, 9, 5, 19, 30), estadio);
        teatro.setDescripcion("Obra clásica de Shakespeare.");
        teatro.publicar();

        eventos.add(concierto);
        eventos.add(teatro);

        Evento festival = EventoFactory.crearConcierto("E003", "Festival Petronio",
                "Cali", LocalDateTime.of(2026, 8, 20, 18, 0), estadio);
        festival.publicar();
        eventos.add(festival);

        Usuario u3 = new Usuario("U003", "Carlos Perez",
                "carlos@gmail.com", "3201112233");
        u3.agregarMetodoDePago("PSE");
        usuarios.add(u3);

        System.out.println("Datos cargados: " + usuarios.size()
                + " usuarios, " + eventos.size() + " eventos.");
    }

    // implementación de IUsuarioRepository
    @Override
    public List<Usuario> obtenerUsuarios() {
        return usuarios;
    }

    @Override
    public Usuario buscarUsuarioPorId(String id) {
        return usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public void agregar(Usuario usuario) {
        usuarios.add(usuario);
    }

    // implementación de IEventoRepository
    @Override
    public List<Evento> obtenerEventos() {
        return eventos;
    }

    @Override
    public Evento buscarEventoPorId(String id) {
        return eventos.stream()
                .filter(e -> e.getIdEvento().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public void agregar(Evento evento) {
        eventos.add(evento);
    }

    // getters para las demás listas
    public List<Recinto>    getRecintos()    { return recintos; }
    public List<Compra>     getCompras()     { return compras; }
    public List<Incidencia> getIncidencias() { return incidencias; }
    public List<Pago>       getPagos()       { return pagos; }

    public void agregarCompra(Compra c)        { compras.add(c); }
    public void agregarIncidencia(Incidencia i) { incidencias.add(i); }
    public void agregarPago(Pago p)            { pagos.add(p); }
}