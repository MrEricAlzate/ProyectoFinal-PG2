package com.Eric.ventaeventos.view;

import com.Eric.ventaeventos.repository.DataInitializer;
import javafx.application.Application;
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

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        DataInitializer.getInstance();
        stage.setTitle("Plataforma de Gestión de Eventos");
        stage.setWidth(700);
        stage.setHeight(500);
        stage.setResizable(false);
        mostrarPantallaInicio();
        stage.show();
    }

    public void mostrarPantallaInicio() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e, #0f3460);");

        Label titulo = new Label("🎟️ EventosUQ");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        titulo.setTextFill(Color.WHITE);

        Label subtitulo = new Label("Plataforma de Gestión de Eventos y Venta de Entradas");
        subtitulo.setFont(Font.font("Arial", 15));
        subtitulo.setTextFill(Color.web("#a0aec0"));

        Label subtitulo2 = new Label("Universidad del Quindío  •  Programación II");
        subtitulo2.setFont(Font.font("Arial", 12));
        subtitulo2.setTextFill(Color.web("#718096"));

        Region separador = new Region();
        separador.setPrefHeight(20);

        Label pregunta = new Label("¿Cómo deseas ingresar?");
        pregunta.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        pregunta.setTextFill(Color.web("#e2e8f0"));

        HBox botones = new HBox(30);
        botones.setAlignment(Pos.CENTER);

        Button btnUsuario = crearBotonRol("👤  Soy Usuario", "#4299e1", "#3182ce");
        Button btnAdmin   = crearBotonRol("🔧  Soy Administrador", "#48bb78", "#38a169");

        btnUsuario.setOnAction(e -> new VistaUsuario(primaryStage, this).mostrar());
        btnAdmin.setOnAction(e -> new VistaAdmin(primaryStage, this).mostrar());

        botones.getChildren().addAll(btnUsuario, btnAdmin);

        Label footer = new Label("Eric Santiago Correa Alzate  •  Eduardo Rodriguez  •  2026");
        footer.setFont(Font.font("Arial", 11));
        footer.setTextFill(Color.web("#4a5568"));

        root.getChildren().addAll(titulo, subtitulo, subtitulo2,
                separador, pregunta, botones, new Region(), footer);

        primaryStage.setScene(new Scene(root, 700, 500));
    }

    private Button crearBotonRol(String texto, String colorNormal, String colorHover) {
        Button btn = new Button(texto);
        btn.setPrefWidth(220);
        btn.setPrefHeight(60);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + colorNormal + "; -fx-background-radius: 12; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + colorHover + "; -fx-background-radius: 12; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + colorNormal + "; -fx-background-radius: 12; -fx-cursor: hand;"));
        return btn;
    }

    public Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) { launch(args); }
}
