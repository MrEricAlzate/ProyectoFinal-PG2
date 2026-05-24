package com.Eric.ventaeventos.view;

import com.Eric.ventaeventos.model.*;
import com.Eric.ventaeventos.repository.DataInitializer;
import com.Eric.ventaeventos.service.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VistaAdmin {

    private final Stage stage;
    private final MainApp mainApp;
    private final DataInitializer data;

    public VistaAdmin(Stage stage, MainApp mainApp) {
        this.stage   = stage;
        this.mainApp = mainApp;
        this.data    = DataInitializer.getInstance();
    }

    public void mostrar() {
        stage.setTitle("Panel de Administración");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f7fafc;");

        HBox header = new HBox();
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #1a1a2e;");
        header.setSpacing(15);

        Label lblTitulo = new Label("🔧 Panel de Administración");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblTitulo.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnVolver = new Button("← Inicio");
        btnVolver.setStyle("-fx-background-color: #4a5568; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> mainApp.mostrarPantallaInicio());
        header.getChildren().addAll(lblTitulo, spacer, btnVolver);
        root.setTop(header);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(
                new Tab("📅  Eventos",     tabEventos()),
                new Tab("🛒  Compras",     tabCompras()),
                new Tab("📊  Reportes",    tabReportes()),
                new Tab("⚠️  Incidencias", tabIncidencias())
        );
        root.setCenter(tabPane);

        stage.setScene(new Scene(root, 800, 580));
        stage.setWidth(800);
    }

    @SuppressWarnings("unchecked")
    private VBox tabEventos() {
        VBox contenido = new VBox(15);
        contenido.setPadding(new Insets(20));

        TableView<Evento> tabla = new TableView<>();
        tabla.setPrefHeight(260);

        TableColumn<Evento, String> colNombre    = col("Nombre",    "nombre",    180);
        TableColumn<Evento, String> colCategoria = col("Categoría", "categoria", 110);
        TableColumn<Evento, String> colCiudad    = col("Ciudad",    "ciudad",    110);
        TableColumn<Evento, String> colEstado    = col("Estado",    "estado",    100);
        TableColumn<Evento, LocalDateTime> colFecha = col("Fecha",  "fechaHora", 160);

        tabla.getColumns().addAll(colNombre, colCategoria, colCiudad, colEstado, colFecha);
        tabla.getItems().addAll(data.obtenerEventos());

        HBox acciones = new HBox(12);
        acciones.setAlignment(Pos.CENTER_LEFT);

        Button btnPublicar = btn("✅ Publicar", "#48bb78");
        Button btnPausar   = btn("⏸️ Pausar",  "#f6ad55");
        Button btnCancelar = btn("❌ Cancelar", "#fc8181");
        Button btnNuevo    = btn("➕ Crear",    "#4299e1");

        btnPublicar.setOnAction(e -> {
            Evento sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { alerta("Selecciona un evento."); return; }
            new PublicarEventoComando(sel).ejecutar();
            tabla.refresh();
        });

        btnPausar.setOnAction(e -> {
            Evento sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { alerta("Selecciona un evento."); return; }
            sel.pausar(); tabla.refresh();
        });

        btnCancelar.setOnAction(e -> {
            Evento sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { alerta("Selecciona un evento."); return; }
            sel.cancelar(); tabla.refresh();
            info("Usuarios suscritos notificados automáticamente.");
        });

        btnNuevo.setOnAction(e -> formularioNuevoEvento(tabla));

        acciones.getChildren().addAll(btnPublicar, btnPausar, btnCancelar, btnNuevo);
        contenido.getChildren().addAll(titulo("Gestión de Eventos"), tabla, acciones);
        return contenido;
    }

    @SuppressWarnings("unchecked")
    private VBox tabCompras() {
        VBox contenido = new VBox(15);
        contenido.setPadding(new Insets(20));

        TableView<Compra> tabla = new TableView<>();
        tabla.setPrefHeight(280);

        TableColumn<Compra, String> colId     = col("ID",     "idCompra", 80);
        TableColumn<Compra, String> colEstado = col("Estado", "estado",   100);
        TableColumn<Compra, Double> colTotal  = col("Total",  "total",    100);

        TableColumn<Compra, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getUsuario() != null ? c.getValue().getUsuario().getNombreCompleto() : "N/A"));
        colUsuario.setPrefWidth(150);

        TableColumn<Compra, String> colEvento = new TableColumn<>("Evento");
        colEvento.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getEvento() != null ? c.getValue().getEvento().getNombre() : "N/A"));
        colEvento.setPrefWidth(160);

        tabla.getColumns().addAll(colId, colUsuario, colEvento, colTotal, colEstado);

        Runnable refrescar = () -> { tabla.getItems().clear(); tabla.getItems().addAll(data.getCompras()); };
        refrescar.run();

        HBox ctrl = new HBox(12);
        ctrl.setAlignment(Pos.CENTER_LEFT);

        Label lblDias = new Label("Días antes:");
        lblDias.setFont(Font.font("Arial", 13));
        Spinner<Integer> spinnerDias = new Spinner<>(0, 60, 7);
        spinnerDias.setPrefWidth(80);

        Button btnCancelarCompra = btn("❌ Cancelar compra seleccionada", "#fc8181");
        btnCancelarCompra.setOnAction(e -> {
            Compra sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { alerta("Selecciona una compra."); return; }
            if (!sel.getEstado().equals("Pagada") && !sel.getEstado().equals("Confirmada")) {
                alerta("Solo se pueden cancelar compras Pagadas o Confirmadas."); return;
            }
            double reembolso = new CompraFacade().cancelarCompra(sel, spinnerDias.getValue());
            tabla.refresh();
            info("Reembolso: $" + String.format("%,.0f", reembolso));
        });

        Button btnRef = btn("🔄 Refrescar", "#4299e1");
        btnRef.setOnAction(e -> refrescar.run());

        ctrl.getChildren().addAll(lblDias, spinnerDias, btnCancelarCompra, btnRef);
        contenido.getChildren().addAll(titulo("Gestión de Compras"), tabla, ctrl);
        return contenido;
    }

    private VBox tabReportes() {
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(25));

        VBox secCSV = seccion("📄 Exportar CSV",
                "Genera reporte_compras.csv en la raíz del proyecto.\nColumnas: ID, Usuario, Evento, Total, Estado.",
                "#ebf8ff", "#bee3f8");
        Button btnCSV = btn("⬇️  Exportar CSV", "#4299e1");
        btnCSV.setOnAction(e -> {
            new ReporteCSVAdapter().exportar(data.getCompras(), "reporte_compras.csv");
            info("CSV exportado: reporte_compras.csv");
        });
        secCSV.getChildren().add(btnCSV);

        VBox secPDF = seccion("📑 Exportar PDF",
                "Genera reporte_compras.pdf usando Apache PDFBox.\nIncluye tabla, fecha y total de registros.",
                "#fff5f5", "#fed7d7");
        Button btnPDF = btn("⬇️  Exportar PDF", "#e53e3e");
        btnPDF.setOnAction(e -> {
            new ReportePDFAdapter().exportar(data.getCompras(), "reporte_compras.pdf");
            info("PDF exportado: reporte_compras.pdf");
        });
        secPDF.getChildren().add(btnPDF);

        contenido.getChildren().addAll(titulo("Exportar Reportes"), secCSV, secPDF);
        return contenido;
    }

    @SuppressWarnings("unchecked")
    private VBox tabIncidencias() {
        VBox contenido = new VBox(15);
        contenido.setPadding(new Insets(20));

        TableView<Incidencia> tabla = new TableView<>();
        tabla.setPrefHeight(330);

        TableColumn<Incidencia, String>        colTipo    = col("Tipo",       "tipo",             130);
        TableColumn<Incidencia, String>        colDesc    = col("Descripción","descripcion",       300);
        TableColumn<Incidencia, String>        colEntidad = col("Entidad",    "entidadAfectada",   100);
        TableColumn<Incidencia, LocalDateTime> colFecha   = col("Fecha",      "fecha",             180);

        tabla.getColumns().addAll(colTipo, colDesc, colEntidad, colFecha);
        tabla.getItems().addAll(data.getIncidencias());

        Button btnRef = btn("🔄 Refrescar", "#4299e1");
        btnRef.setOnAction(e -> { tabla.getItems().clear(); tabla.getItems().addAll(data.getIncidencias()); });

        contenido.getChildren().addAll(titulo("Registro de Incidencias"), tabla, btnRef);
        return contenido;
    }

    private void formularioNuevoEvento(TableView<Evento> tabla) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Crear nuevo evento");
        dialog.setHeaderText("Ingresa los datos del nuevo evento");

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(12); grid.setPadding(new Insets(20));

        TextField txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        TextField txtCiudad = new TextField(); txtCiudad.setPromptText("Ciudad");
        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("Concierto", "Teatro", "Conferencia");
        cmbTipo.setValue("Concierto");

        grid.add(new Label("Nombre:"), 0, 0); grid.add(txtNombre, 1, 0);
        grid.add(new Label("Ciudad:"), 0, 1); grid.add(txtCiudad, 1, 1);
        grid.add(new Label("Tipo:"),   0, 2); grid.add(cmbTipo,   1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result != ButtonType.OK) return;
            String nombre = txtNombre.getText().trim();
            String ciudad = txtCiudad.getText().trim();
            if (nombre.isEmpty() || ciudad.isEmpty()) { alerta("Nombre y ciudad son obligatorios."); return; }

            Recinto recinto = data.getRecintos().isEmpty() ? null : data.getRecintos().get(0);
            String id = "E" + String.format("%03d", data.obtenerEventos().size() + 1);

            Evento nuevo = switch (cmbTipo.getValue()) {
                case "Teatro"      -> EventoFactory.crearTeatro(id, nombre, ciudad, LocalDateTime.now().plusMonths(3), recinto);
                case "Conferencia" -> EventoFactory.crearConferencia(id, nombre, ciudad, LocalDateTime.now().plusMonths(2), recinto);
                default            -> EventoFactory.crearConcierto(id, nombre, ciudad, LocalDateTime.now().plusMonths(4), recinto);
            };

            data.agregar(nuevo);
            tabla.getItems().add(nuevo);
            info("Evento creado en estado Borrador: " + nombre);
        });
    }

    // ── helpers ──────────────────────────────────────────────────────

    private <S, T> TableColumn<S, T> col(String texto, String propiedad, double ancho) {
        TableColumn<S, T> c = new TableColumn<>(texto);
        c.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        c.setPrefWidth(ancho);
        return c;
    }

    private Label titulo(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbl.setTextFill(Color.web("#2d3748"));
        return lbl;
    }

    private Button btn(String texto, String color) {
        Button b = new Button(texto);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 14 8 14;");
        return b;
    }

    private VBox seccion(String titulo, String desc, String bg, String border) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(18));
        box.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 12; -fx-border-color: " + border + "; -fx-border-radius: 12;");
        Label t = new Label(titulo); t.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label d = new Label(desc);   d.setFont(Font.font("Arial", 13)); d.setWrapText(true); d.setTextFill(Color.web("#4a5568"));
        box.getChildren().addAll(t, d);
        return box;
    }

    private void alerta(String msg) { Alert a = new Alert(Alert.AlertType.WARNING);  a.setHeaderText(null); a.setContentText(msg); a.showAndWait(); }
    private void info(String msg)   { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setHeaderText(null); a.setContentText(msg); a.showAndWait(); }
}