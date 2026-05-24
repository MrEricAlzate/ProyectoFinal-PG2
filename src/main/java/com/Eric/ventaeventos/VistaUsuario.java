package com.Eric.ventaeventos.view;

import com.Eric.ventaeventos.model.Evento;
import com.Eric.ventaeventos.repository.DataInitializer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class VistaUsuario {

    private final Stage stage;
    private final MainApp mainApp;
    private final DataInitializer data;

    public VistaUsuario(Stage stage, MainApp mainApp) {
        this.stage   = stage;
        this.mainApp = mainApp;
        this.data    = DataInitializer.getInstance();
    }

    public void mostrar() {
        stage.setTitle("Eventos disponibles — Usuario");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f7fafc;");

        HBox header = new HBox();
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #1a1a2e;");
        header.setSpacing(15);

        Label lblTitulo = new Label("🎟️ EventosUQ  —  Catálogo de Eventos");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblTitulo.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnVolver = new Button("← Inicio");
        btnVolver.setStyle("-fx-background-color: #4a5568; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> mainApp.mostrarPantallaInicio());
        header.getChildren().addAll(lblTitulo, spacer, btnVolver);

        HBox filtros = new HBox(15);
        filtros.setPadding(new Insets(15, 25, 10, 25));
        filtros.setAlignment(Pos.CENTER_LEFT);
        filtros.setStyle("-fx-background-color: #edf2f7; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Nombre del evento...");
        txtBuscar.setPrefWidth(200);

        ComboBox<String> cmbCategoria = new ComboBox<>();
        cmbCategoria.getItems().addAll("Todas", "Concierto", "Teatro", "Conferencia");
        cmbCategoria.setValue("Todas");
        cmbCategoria.setPrefWidth(140);

        Button btnFiltrar = new Button("🔍 Filtrar");
        btnFiltrar.setStyle("-fx-background-color: #4299e1; -fx-text-fill: white; -fx-background-radius: 8;");

        filtros.getChildren().addAll(new Label("Buscar:"), txtBuscar,
                new Label("Categoría:"), cmbCategoria, btnFiltrar);

        root.setTop(new VBox(header, filtros));

        VBox listaEventos = new VBox(12);
        listaEventos.setPadding(new Insets(20, 25, 20, 25));

        ScrollPane scroll = new ScrollPane(listaEventos);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        root.setCenter(scroll);

        cargarEventos(listaEventos, "", "Todas");

        btnFiltrar.setOnAction(e -> cargarEventos(listaEventos, txtBuscar.getText().trim(), cmbCategoria.getValue()));
        txtBuscar.textProperty().addListener((obs, old, nv) -> cargarEventos(listaEventos, nv, cmbCategoria.getValue()));

        stage.setScene(new Scene(root, 700, 560));
        stage.setHeight(560);
    }

    private void cargarEventos(VBox contenedor, String busqueda, String categoria) {
        contenedor.getChildren().clear();

        List<Evento> eventos = data.obtenerEventos().stream()
                .filter(e -> e.getEstado().equals("Publicado"))
                .filter(e -> busqueda.isEmpty() || e.getNombre().toLowerCase().contains(busqueda.toLowerCase()))
                .filter(e -> categoria.equals("Todas") || e.getCategoria().equals(categoria))
                .collect(Collectors.toList());

        if (eventos.isEmpty()) {
            Label lbl = new Label("No se encontraron eventos con esos criterios.");
            lbl.setFont(Font.font("Arial", 14));
            lbl.setTextFill(Color.web("#718096"));
            contenedor.getChildren().add(lbl);
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");

        for (Evento ev : eventos) {
            HBox tarjeta = new HBox(15);
            tarjeta.setPadding(new Insets(18, 20, 18, 20));
            tarjeta.setAlignment(Pos.CENTER_LEFT);
            tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);");

            String icono = switch (ev.getCategoria()) {
                case "Concierto"   -> "🎵";
                case "Teatro"      -> "🎭";
                case "Conferencia" -> "🎤";
                default            -> "📅";
            };
            Label lblIcono = new Label(icono);
            lblIcono.setFont(Font.font(32));

            VBox info = new VBox(4);
            HBox.setHgrow(info, Priority.ALWAYS);

            Label lblNombre = new Label(ev.getNombre());
            lblNombre.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            Label lblDetalle = new Label(ev.getCategoria() + "  •  " + ev.getCiudad() + "  •  " + ev.getFechaHora().format(fmt));
            lblDetalle.setFont(Font.font("Arial", 13));
            lblDetalle.setTextFill(Color.web("#718096"));

            info.getChildren().addAll(lblNombre, lblDetalle);

            if (ev.getDescripcion() != null) {
                Label lblDesc = new Label(ev.getDescripcion());
                lblDesc.setFont(Font.font("Arial", 12));
                lblDesc.setTextFill(Color.web("#a0aec0"));
                info.getChildren().add(lblDesc);
            }

            double precioMin = ev.getRecinto() != null && !ev.getRecinto().getZonas().isEmpty()
                    ? ev.getRecinto().getZonas().stream().mapToDouble(z -> z.getPrecioBase()).min().orElse(0) : 0;

            VBox precioBox = new VBox(4);
            precioBox.setAlignment(Pos.CENTER_RIGHT);

            Label lblDesde = new Label("Desde");
            lblDesde.setFont(Font.font("Arial", 11));
            lblDesde.setTextFill(Color.web("#a0aec0"));

            Label lblPrecio = new Label("$" + String.format("%,.0f", precioMin));
            lblPrecio.setFont(Font.font("Arial", FontWeight.BOLD, 15));

            Button btnComprar = new Button("Comprar →");
            btnComprar.setStyle("-fx-background-color: #4299e1; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;");

            final Evento evFinal = ev;
            btnComprar.setOnAction(e -> new VistaCompra(stage, mainApp, this, evFinal).mostrar());

            precioBox.getChildren().addAll(lblDesde, lblPrecio, btnComprar);
            tarjeta.getChildren().addAll(lblIcono, info, precioBox);
            contenedor.getChildren().add(tarjeta);
        }
    }
}
