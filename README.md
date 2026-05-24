# Plataforma de Gestión de Eventos y Venta de Entradas
Proyecto Final — Programación II (PGII) | Universidad del Quindío

## Integrantes del grupo

- Eric Santiago Correa Alzate
- Eduardo Rodriguez

## Descripción del proyecto

Plataforma de gestión de eventos y venta de entradas desarrollada en Java 21 con JavaFX. Permite a los usuarios explorar eventos como conciertos, teatro y conferencias, seleccionar zonas y asientos, comprar entradas y agregar servicios adicionales como acceso VIP o seguro de cancelación. Los administradores pueden gestionar el catálogo de eventos, recintos, zonas, asientos, registrar incidencias y exportar reportes en CSV y PDF.

El proyecto aplica principios SOLID, nueve patrones de diseño GOF y está estructurado en capas con Maven.

## Requisitos para ejecutar

- Java 21 o superior
- Maven 3.8 o superior
- IntelliJ IDEA recomendado, o cualquier IDE con soporte Maven

## Instrucciones para compilar y ejecutar

Clonar el repositorio:

    git clone https://github.com/MrEricAlzate/ProyectoFinal-PG2.git
    cd ProyectoFinal-PG2

Compilar con Maven:

    mvn clean compile

Ejecutar la aplicación:

    mvn javafx:run

También se puede ejecutar abriendo el proyecto en IntelliJ IDEA y corriendo la clase Main.java directamente con el botón de ejecución.

Las dependencias principales son JavaFX 23, Apache PDFBox 3.0.2 para reportes PDF y Apache POI 5.3.0 para reportes Excel. Maven las descarga automáticamente al compilar.

## Estructura del proyecto

    src/main/java/com/Eric/ventaeventos/
        model/         Entidades: Usuario, Evento, Recinto, Zona, Asiento, Compra, Entrada, Pago, Incidencia
        repository/    DataInitializer, IUsuarioRepository, IEventoRepository
        service/       Lógica de negocio y patrones de diseño
            strategy/  CancelacionStrategy, ReembolsoCompletoStrategy, ReembolsoParcialStrategy
        view/          Pantallas JavaFX para usuario y administrador

## Patrones de diseño implementados

### Patrones creacionales

**Singleton aplicado en DataInitializer**

Requisito que resuelve: RF-049, gestión centralizada de datos de prueba y acceso global al sistema.

Problema: si se crean múltiples instancias de DataInitializer, cada módulo tendría su propia lista de usuarios y eventos desincronizada con el resto de la aplicación.

Propósito: garantizar una única instancia en toda la aplicación con un punto de acceso global. El constructor es privado para que nadie pueda instanciarla desde afuera.

```java
public class DataInitializer implements IUsuarioRepository, IEventoRepository {

    private static DataInitializer instance;

    private DataInitializer() { cargarDatos(); }

    public static DataInitializer getInstance() {
        if (instance == null) {
            instance = new DataInitializer();
        }
        return instance;
    }
}
```

**Factory Method aplicado en EventoFactory**

Requisito que resuelve: RF-013, RF-049, creación de eventos de distintas categorías con configuraciones específicas.

Problema: cada tipo de evento tiene políticas de cancelación distintas. Sin Factory habría código repetido en cada parte del proyecto donde se crea un evento y sería fácil olvidar configurar algo.

Propósito: centralizar la creación de eventos con sus valores predeterminados según el tipo, de forma que agregar un nuevo tipo solo requiere un método nuevo sin tocar el resto del código.

```java
public static Evento crearConcierto(String id, String nombre,
                                    String ciudad, LocalDateTime fecha,
                                    Recinto recinto) {
    Evento e = new Evento(id, nombre, "Concierto", ciudad, fecha, recinto);
    e.setPoliticaCancelacion("Reembolso 100% con más de 7 días.");
    e.setPoliticaReembolso("Reembolso parcial entre 3 y 7 días.");
    return e;
}
```

**Builder aplicado en CompraBuilder**

Requisito que resuelve: RF-006, RF-009, creación de compras con múltiples entradas y servicios opcionales.

Problema: una Compra puede tener muchos campos opcionales como entradas, asientos numerados, VIP o seguro. Un constructor con todos esos parámetros a la vez es ilegible y propenso a errores.

Propósito: construir una Compra paso a paso encadenando solo los pasos que se necesitan, mejorando la legibilidad y la flexibilidad.

```java
Compra compra = new CompraBuilder(usuario, evento)
        .conEntrada(zonaVIP)
        .conAsiento(zonaVIP, asientoA3)
        .conServicioVIP()
        .conSeguroCancelacion()
        .build();
```

### Patrones estructurales

**Decorator aplicado en ServicioDecorator (obligatorio)**

Requisito que resuelve: RF-009, agregar servicios adicionales a una compra como VIP, seguro y merchandising.

Problema: agregar servicios modificando la clase Compra violaría el principio OCP. Con herencia las combinaciones posibles crecen exponencialmente.

Propósito: extender el comportamiento de una compra en tiempo de ejecución sin modificar las clases existentes, sumando el costo y la descripción de cada servicio de forma encadenada.

```java
// la cadena suma precios automáticamente
CompraComponent base = new CompraBase(compra);     // 180.000
CompraComponent vip  = new ServicioVIP(base);      // + 50.000
CompraComponent seg  = new SeguroCancelacion(vip); // + 15.000
// seg.getTotal() devuelve 245.000
// seg.getDescripcion() devuelve "Compra base + VIP + Seguro Cancelación"
```

**Facade aplicado en CompraFacade**

Requisito que resuelve: RF-007, RF-050, simplificar el proceso de compra para la pantalla JavaFX.

Problema: realizar una compra requería coordinar Compra, Pago, Entrada, Asiento y DataInitializer desde la pantalla, acoplando la interfaz gráfica con toda la lógica de negocio.

Propósito: exponer un método simple que coordine internamente todos los pasos, de forma que la pantalla solo conozca CompraFacade y no las clases internas.

```java
// la pantalla JavaFX solo necesita esto
CompraFacade facade = new CompraFacade();
Compra c = facade.realizarCompra(usuario, evento, zona, "Nequi");

// internamente coordina Builder, Pago y DataInitializer
```

**Adapter aplicado en ReporteCSVAdapter y ReportePDFAdapter**

Requisito que resuelve: RF-046, RF-050, exportar reportes en CSV y PDF desde una interfaz común.

Problema: FileWriter y PDFBox tienen APIs completamente distintas. Sin Adapter el código de reportes quedaría acoplado a una librería específica y sería difícil agregar nuevos formatos.

Propósito: definir un contrato único ReporteExportador para que la pantalla no dependa de ninguna librería concreta. Si se agrega un formato nuevo solo se crea un adaptador nuevo.

```java
ReporteExportador exportador = new ReporteCSVAdapter();
exportador.exportar(compras, "reporte.csv");

exportador = new ReportePDFAdapter();
exportador.exportar(compras, "reporte.pdf");
```

### Patrones de comportamiento

**Strategy aplicado en CancelacionStrategy (obligatorio)**

Requisito que resuelve: RF-006, RF-036, RF-051, cancelar compras aplicando diferentes políticas de reembolso según el evento.

Problema: cada evento puede tener reglas distintas de devolución. Sin Strategy habría un bloque condicional gigante en CompraService que habría que modificar cada vez que se agregue una política nueva, violando OCP.

Propósito: permitir cambiar el algoritmo de cancelación en tiempo de ejecución sin modificar CompraService. Cada política es una clase independiente.

```java
CompraService servicio = new CompraService();

// política generosa por defecto
servicio.cancelarCompra(compra, 10); // devuelve el 100%

// cambiar la política sin modificar CompraService
servicio.setCancelacionStrategy(new ReembolsoParcialStrategy());
servicio.cancelarCompra(compra, 3);  // devuelve el 0%
```

**Observer aplicado en EventoObserver**

Requisito que resuelve: RF-008, RF-017, RF-051, notificar a los usuarios cuando un evento cambia de estado.

Problema: sin Observer, al cancelar un evento habría que recorrer manualmente todas las compras y notificar a cada usuario desde la clase Evento, acoplándola con la lógica de usuarios.

Propósito: los usuarios se suscriben como observers del evento al comprar entradas. Cuando el estado cambia, Evento notifica automáticamente a todos los suscritos sin saber quiénes son.

```java
evento.agregarObserver(new NotificacionUsuario(eric));
evento.agregarObserver(new NotificacionUsuario(maria));

// al cancelar, ambos reciben la notificación automáticamente
evento.cancelar();
```

**Command aplicado en PublicarEventoComando**

Requisito que resuelve: RF-013, RF-017, RF-051, acciones del administrador ejecutables y reversibles.

Problema: si el admin publica un evento por error no hay forma de deshacer la acción sin agregar lógica de reversión en cada pantalla.

Propósito: encapsular cada acción como un objeto que sabe ejecutarse y deshacerse. El estado anterior se guarda antes de ejecutar para poder revertirlo.

```java
Comando cmd = new PublicarEventoComando(evento);

cmd.ejecutar();  // el evento pasa a Publicado
cmd.deshacer();  // el evento vuelve al estado anterior
```

## Principios SOLID aplicados

**SRP: cada clase tiene una sola responsabilidad**

La clase Compra originalmente tenía lógica de cancelación mezclada con los datos. Se separó esa lógica en CompraService. Ahora Compra solo guarda datos y CompraService decide qué se puede hacer con ellos.

```java
// Compra.java solo guarda datos
public class Compra {
    private String estado;
    private double totalBase;
    private List<Entrada> entradas;
}

// CompraService.java tiene la lógica
public class CompraService {
    public double cancelarCompra(Compra compra, int dias) { ... }
    public void confirmarCompra(Compra compra) { ... }
}
```

**OCP: abierto para extensión, cerrado para modificación**

Strategy y Decorator permiten agregar comportamientos sin tocar clases existentes. Para agregar una nueva política de reembolso se crea una clase nueva que implemente CancelacionStrategy sin modificar CompraService.

```java
public class ReembolsoEventoCancelado implements CancelacionStrategy {
    public double calcularReembolso(double monto, int dias) { return monto; }
    public String getDescripcionPolitica() { return "Evento cancelado: reembolso total"; }
}
```

**LSP: las subclases pueden reemplazar a la clase padre sin romper nada**

ReembolsoCompletoStrategy y ReembolsoParcialStrategy implementan CancelacionStrategy y se pueden usar indistintamente en CompraService. ServicioVIP y SeguroCancelacion reemplazan a ServicioDecorator sin romper el programa.

```java
CancelacionStrategy politica = new ReembolsoCompletoStrategy();
// o
CancelacionStrategy politica = new ReembolsoParcialStrategy();
servicio.setCancelacionStrategy(politica); // funciona igual con cualquiera
```

**ISP: interfaces pequeñas y específicas**

En vez de una interfaz grande con todo, se crearon interfaces separadas por responsabilidad. Ninguna clase está obligada a implementar métodos que no usa.

```java
public interface IUsuarioRepository {
    List<Usuario> obtenerUsuarios();
    Usuario buscarUsuarioPorId(String id);
    void agregar(Usuario usuario);
}

public interface IEventoRepository { ... }
public interface IReportable { String generarResumen(); String exportarCSV(); }
public interface INotificable { void notificar(String mensaje); }
```

**DIP: depender de abstracciones, no de implementaciones concretas**

DataInitializer implementa IUsuarioRepository e IEventoRepository. Los servicios dependen de esas interfaces, no de DataInitializer directamente. Si en el futuro se conecta una base de datos real, solo se crea una nueva implementación sin cambiar los servicios.

```java
public class DataInitializer implements IUsuarioRepository, IEventoRepository {
    public List<Usuario> obtenerUsuarios() { return usuarios; }
    public List<Evento> obtenerEventos() { return eventos; }
}

// los servicios dependen de la interfaz
IUsuarioRepository repo = DataInitializer.getInstance();
```

## Diagrama de clases

El diagrama completo está en el archivo diagrama_clases.puml en la raíz del repositorio. Cubre las entidades Usuario, Evento, Recinto, Zona, Asiento, Compra, Entrada, Pago e Incidencia, y las clases de soporte de patrones como CompraComponent, ServicioDecorator, CancelacionStrategy, EventoObserver, Comando y ReporteExportador.

Para visualizarlo se puede instalar el plugin PlantUML Integration en IntelliJ IDEA y abrir el archivo directamente.

## Datos de prueba inicializados

DataInitializer carga automáticamente al iniciar la aplicación los siguientes datos:

- Tres usuarios con métodos de pago como tarjeta débito, Nequi, PSE y tarjeta crédito
- Un recinto (Estadio Centenario, Armenia) con tres zonas y asientos VIP numerados
- Tres eventos publicados: Concierto Juanes, Romeo y Julieta, Festival Petronio
- Listas vacías listas para recibir compras, pagos e incidencias en tiempo de ejecución

## Reportes operativos

La aplicación permite exportar reportes desde el menú de administrador. El formato CSV usa ReporteCSVAdapter con columnas de ID, usuario, evento, total y estado. El formato PDF usa ReportePDFAdapter con tabla formateada, fecha de generación y total de registros. Ambos adaptadores implementan la interfaz ReporteExportador aplicando el patrón Adapter.
