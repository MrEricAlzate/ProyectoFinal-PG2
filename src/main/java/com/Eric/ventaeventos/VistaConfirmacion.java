package com.Eric.ventaeventos.view;

import com.Eric.ventaeventos.model.Compra;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class VistaConfirmacion {

    private final Stage stage;
    private final MainApp mainApp;
    private final VistaUsuario vistaUsuario;
    private final Compra compra;

    public VistaConfirmacion(Stage stage, MainApp mainApp, VistaUsuario vistaUsuario, Compra compra) {
        this.stage        = stage;
        this.mainApp      = mainApp;
        this.vistaUsuario = vistaUsuario;
        this.compra       = compra;
    }

    public void mostrar() {
        stage.setTitle("¡Compra exitosa!");

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50, 60, 50, 60));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f0fff4, #c6f6d5);");

        Label lblIcono = new Label("✅");
        lblIcono.setFont(Font.font(60));

        Label lblTitulo = new Label("¡Compra realizada con éxito!");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        lblTitulo.setTextFill(Color.web("#276749"));

        VBox tarjeta = new VBox(12);
        tarjeta.setPadding(new Insets(25, 35, 25, 35));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");
        tarjeta.setMaxWidth(420);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");

        tarjeta.getChildren().addAll(
                fila("🎫 ID de Compra:", compra.getIdCompra()),
                fila("👤 Usuario:", compra.getUsuario() != null ? compra.getUsuario().getNombreCompleto() : "N/A"),
                fila("📅 Evento:", compra.getEvento() != null ? compra.getEvento().getNombre() : "N/A"),
                fila("🕐 Fecha:", compra.getFechaCreacion().format(fmt)),
                fila("💰 Total pagado:", "$" + String.format("%,.0f", compra.getTotal())),
                fila("📋 Estado:", compra.getEstado())
        );

        HBox botones = new HBox(20);
        botones.setAlignment(Pos.CENTER);

        Button btnMas = new Button("🔍  Ver más eventos");
        btnMas.setPrefWidth(180); btnMas.setPrefHeight(44);
        btnMas.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btnMas.setStyle("-fx-background-color: #4299e1; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");
        btnMas.setOnAction(e -> vistaUsuario.mostrar());

        Button btnInicio = new Button("🏠  Ir al Inicio");
        btnInicio.setPrefWidth(160); btnInicio.setPrefHeight(44);
        btnInicio.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btnInicio.setStyle("-fx-background-color: #718096; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");
        btnInicio.setOnAction(e -> mainApp.mostrarPantallaInicio());

        botones.getChildren().addAll(btnMas, btnInicio);
        root.getChildren().addAll(lblIcono, lblTitulo, tarjeta, botones);

        stage.setScene(new Scene(root, 700, 520));
        stage.setHeight(520);
    }

    private Label fila(String clave, String valor) {
        Label lbl = new Label(clave + "  " + valor);
        lbl.setFont(Font.font("Arial", 14));
        lbl.setWrapText(true);
        return lbl;
    }
}