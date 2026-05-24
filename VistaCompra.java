package com.Eric.ventaeventos.view;

import com.Eric.ventaeventos.model.*;
import com.Eric.ventaeventos.repository.DataInitializer;
import com.Eric.ventaeventos.service.*;
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

public class VistaCompra {

    private final Stage stage;
    private final MainApp mainApp;
    private final VistaUsuario vistaUsuario;
    private final Evento evento;
    private final DataInitializer data;

    private ComboBox<String> cmbUsuario;
    private ComboBox<String> cmbZona;
    private ComboBox<String> cmbMetodoPago;
    private CheckBox chkVIP;
    private CheckBox chkSeguro;
    private Label lblTotal;

    public VistaCompra(Stage stage, MainApp mainApp, VistaUsuario vistaUsuario, Evento evento) {
        this.stage        = stage;
        this.mainApp      = mainApp;
        this.vistaUsuario = vistaUsuario;
        this.evento       = evento;
        this.data         = DataInitializer.getInstance();
    }

    public void mostrar() {
        stage.setTitle("Comprar entrada — " + evento.getNombre());

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f7fafc;");

        HBox header = new HBox();
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #1a1a2e;");
        header.setSpacing(15);

        Label lblTitulo = new Label("🎟️ Comprar Entrada");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblTitulo.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: #4a5568; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> vistaUsuario.mostrar());
        header.getChildren().addAll(lblTitulo, spacer, btnVolver);
        root.setTop(header);

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(25, 40, 25, 40));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");
        VBox infoEvento = new VBox(6);
        infoEvento.setPadding(new Insets(18));
        infoEvento.setStyle("-fx-background-color: #ebf8ff; -fx-background-radius: 12; -fx-border-color: #bee3f8; -fx-border-radius: 12;");

        Label lblNombreEvento = new Label(evento.getNombre());
        lblNombreEvento.setFont(Font.font("Arial", FontWeight.BOLD, 17));

        Label lblDetalleEvento = new Label(evento.getCategoria() + "  •  " + evento.getCiudad() + "  •  " + evento.getFechaHora().format(fmt));
        lblDetalleEvento.setTextFill(Color.web("#2b6cb0"));
        infoEvento.getChildren().addAll(lblNombreEvento, lblDetalleEvento);

        if (evento.getPoliticaCancelacion() != null) {
            Label lblPol = new Label("📋 " + evento.getPoliticaCancelacion());
            lblPol.setFont(Font.font("Arial", 12));
            lblPol.setTextFill(Color.web("#4a5568"));
            infoEvento.getChildren().add(lblPol);
        }

        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 2);");

        int fila = 0;

        form.add(labelForm("👤 Usuario:"), 0, fila);
        cmbUsuario = new ComboBox<>();
        data.obtenerUsuarios().forEach(u -> cmbUsuario.getItems().add(u.getIdUsuario() + " — " + u.getNombreCompleto()));
        if (!cmbUsuario.getItems().isEmpty()) cmbUsuario.setValue(cmbUsuario.getItems().get(0));
        cmbUsuario.setPrefWidth(280);
        form.add(cmbUsuario, 1, fila++);

        form.add(labelForm("🏟️ Zona:"), 0, fila);
        cmbZona = new ComboBox<>();
        if (evento.getRecinto() != null) {
            evento.getRecinto().getZonas().forEach(z ->
                    cmbZona.getItems().add(z.getIdZona() + " — " + z.getNombre() + "  ($" + String.format("%,.0f", z.getPrecioBase()) + ")"));
        }
        if (!cmbZona.getItems().isEmpty()) cmbZona.setValue(cmbZona.getItems().get(0));
        cmbZona.setPrefWidth(280);
        form.add(cmbZona, 1, fila++);

        form.add(labelForm("💳 Método de pago:"), 0, fila);
        cmbMetodoPago = new ComboBox<>();
        cmbMetodoPago.getItems().addAll("Tarjeta Débito", "Tarjeta Crédito", "Nequi", "PSE", "Efectivo");
        cmbMetodoPago.setValue("Nequi");
        cmbMetodoPago.setPrefWidth(280);
        form.add(cmbMetodoPago, 1, fila++);

        form.add(labelForm("➕ Servicios adicionales:"), 0, fila);
        VBox servicios = new VBox(8);
        chkVIP    = new CheckBox("🌟 Acceso VIP  (+$50.000)");
        chkSeguro = new CheckBox("🛡️ Seguro de cancelación  (+$15.000)");
        chkVIP.setFont(Font.font("Arial", 13));
        chkSeguro.setFont(Font.font("Arial", 13));
        servicios.getChildren().addAll(chkVIP, chkSeguro);
        form.add(servicios, 1, fila++);

        form.add(labelForm("💰 Total estimado:"), 0, fila);
        lblTotal = new Label("$0");
        lblTotal.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        form.add(lblTotal, 1, fila++);

        cmbZona.setOnAction(e -> actualizarTotal());
        chkVIP.setOnAction(e -> actualizarTotal());
        chkSeguro.setOnAction(e -> actualizarTotal());
        actualizarTotal();

        Button btnConfirmar = new Button("✅  Confirmar Compra");
        btnConfirmar.setPrefWidth(220);
        btnConfirmar.setPrefHeight(48);
        btnConfirmar.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        btnConfirmar.setStyle("-fx-background-color: #48bb78; -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand;");
        btnConfirmar.setOnAction(e -> confirmarCompra());

        HBox botones = new HBox(btnConfirmar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        contenido.getChildren().addAll(infoEvento, form, botones);

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        root.setCenter(scroll);

        stage.setScene(new Scene(root, 700, 580));
        stage.setHeight(580);
    }

    private Label labelForm(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lbl.setMinWidth(170);
        return lbl;
    }

    private void actualizarTotal() {
        if (cmbZona.getValue() == null) return;
        String idZona = cmbZona.getValue().split(" — ")[0];
        Zona zona = evento.getRecinto().getZonas().stream()
                .filter(z -> z.getIdZona().equals(idZona)).findFirst().orElse(null);
        if (zona == null) return;
        double total = zona.getPrecioBase();
        if (chkVIP.isSelected())    total += 50000;
        if (chkSeguro.isSelected()) total += 15000;
        lblTotal.setText("$" + String.format("%,.0f", total));
    }

    private void confirmarCompra() {
        if (cmbUsuario.getValue() == null || cmbZona.getValue() == null) {
            alerta("Por favor selecciona usuario y zona."); return;
        }
        String idUsuario = cmbUsuario.getValue().split(" — ")[0];
        String idZona    = cmbZona.getValue().split(" — ")[0];
        Usuario usuario  = data.buscarUsuarioPorId(idUsuario);
        Zona zona = evento.getRecinto().getZonas().stream()
                .filter(z -> z.getIdZona().equals(idZona)).findFirst().orElse(null);
        if (usuario == null || zona == null) { alerta("No se encontró el usuario o la zona."); return; }

        CompraFacade facade = new CompraFacade();
        Compra compra = facade.realizarCompra(usuario, evento, zona, cmbMetodoPago.getValue());
        if (compra == null) { alerta("No se pudo procesar la compra."); return; }

        if (chkVIP.isSelected())
            compra.agregarServicioAdicional(new ServicioVIP(compra.componenteActual()));
        if (chkSeguro.isSelected())
            compra.agregarServicioAdicional(new SeguroCancelacion(compra.componenteActual()));

        new VistaConfirmacion(stage, mainApp, vistaUsuario, compra).mostrar();
    }

    private void alerta(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}