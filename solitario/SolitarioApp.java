package com.example.solitario;

import solitaire.*;
import com.example.solitario.GUI.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación Solitario Grafico.
 * Implementa la interfaz de usuario, el sistema de clics y la función Undo.
 * * @author Saúl Iván Ramírez Heraldez
 * @version 2026-03-18
 */
public class SolitarioApp extends Application {
    private SolitaireGame juego;
    private Pane root;

    private enum TipoSeleccion { NINGUNA, WASTE, TABLEAU }
    private TipoSeleccion seleccionActual = TipoSeleccion.NINGUNA;
    private int indiceSeleccionado = -1;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        root.setStyle("-fx-background-color: #8B0000;");

        juego = new SolitaireGame();
        renderizarTablero();

        Scene escena = new Scene(root, 1100, 850);
        primaryStage.setTitle("Solitario Pro - Corrección");
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    /**
     * Dibuja todos los componentes del juego en pantalla.
     * Se invoca después de cada movimiento o acción de deshacer.
     */
    public void renderizarTablero() {
        root.getChildren().clear();

        Button btnUndo = new Button("Deshacer Movimiento");
        btnUndo.setPrefSize(200, 40);
        btnUndo.setLayoutX(550 - 100);
        btnUndo.setLayoutY(780);
        btnUndo.setStyle("-fx-base: #333; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");

        btnUndo.setOnAction(e -> {
            juego.deshacer();
            cancelarSeleccion();
            renderizarTablero();
        });
        root.getChildren().add(btnUndo);

        MazoGrafico drawVisual = new MazoGrafico(juego.getDrawPile().getCartas());
        drawVisual.setLayoutX(50); drawVisual.setLayoutY(50);
        drawVisual.setOnMouseClicked(e -> {
            cancelarSeleccion();
            if (juego.getDrawPile().hayCartas()) {
                juego.drawCards();
            } else {
                juego.reloadDrawPile();
            }
            renderizarTablero();
        });
        root.getChildren().add(drawVisual);

        if (juego.getWastePile().hayCartas()) {
            CartaGrafica wasteVisual = new CartaGrafica(juego.getWastePile().verCarta());
            wasteVisual.setLayoutX(175); wasteVisual.setLayoutY(50);
            wasteVisual.setResaltado(seleccionActual == TipoSeleccion.WASTE);
            wasteVisual.setOnMouseClicked(e -> manejarClicWaste());
            root.getChildren().add(wasteVisual);
        }

        //
        for (int i = 0; i < 4; i++) {
            FoundationDeck fd = juego.getFoundations().get(i);
            FoundationGrafico fg = new FoundationGrafico(fd);
            fg.setLayoutX(450 + (i * 130)); fg.setLayoutY(50);
            fg.setOnMouseClicked(e -> manejarClicFoundation());
            root.getChildren().add(fg);
        }

        for (int i = 0; i < 7; i++) {
            TableauDeck td = juego.getTableau().get(i);
            TableauDeckGrafico tg = new TableauDeckGrafico(td.getCardsAsList());
            tg.setLayoutX(50 + (i * 140)); tg.setLayoutY(250);

            if (seleccionActual == TipoSeleccion.TABLEAU && indiceSeleccionado == (i + 1)) {
                tg.setResaltarUltima(true);
            }

            final int numColumna = i + 1;
            tg.setOnMouseClicked(e -> manejarClicTableau(numColumna));
            root.getChildren().add(tg);
        }

        if (juego.isGameOver()) {
            mostrarAlertaVictoria();
        }
    }

// ------------------------------------ ZONA DE CLICKS ------------------------------------

    private void manejarClicWaste() {
        if (seleccionActual == TipoSeleccion.NINGUNA) {
            seleccionActual = TipoSeleccion.WASTE;
        } else {
            cancelarSeleccion();
        }
        renderizarTablero();
    }

    private void manejarClicTableau(int numero) {
        if (seleccionActual == TipoSeleccion.NINGUNA) {
            if (!juego.getTableau().get(numero - 1).isEmpty()) {
                seleccionActual = TipoSeleccion.TABLEAU;
                indiceSeleccionado = numero;
            }
        } else {
            boolean exito = false;
            if (seleccionActual == TipoSeleccion.WASTE) {
                exito = juego.moveWasteToTableau(numero);
            } else if (seleccionActual == TipoSeleccion.TABLEAU) {
                exito = juego.moveTableauToTableau(indiceSeleccionado, numero);
            }
            cancelarSeleccion();
        }
        renderizarTablero();
    }

    private void manejarClicFoundation() {
        if (seleccionActual != TipoSeleccion.NINGUNA) {
            boolean exito = false;
            if (seleccionActual == TipoSeleccion.WASTE) {
                exito = juego.moveWasteToFoundation();
            } else if (seleccionActual == TipoSeleccion.TABLEAU) {
                exito = juego.moveTableauToFoundation(indiceSeleccionado);
            }

            if (exito) {
                System.out.println("Movimiento a Foundation exitoso");
            }

            cancelarSeleccion();
            renderizarTablero();
        }
    }

    // -------------------------------------------------------------------------------------------

    private void cancelarSeleccion() {
        seleccionActual = TipoSeleccion.NINGUNA;
        indiceSeleccionado = -1;
    }

    /**
     * Muestra una ventana emergente cuando el jugador gana.
     */
    private void mostrarAlertaVictoria() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Victoria!");
        alert.setHeaderText("¡Felicidades, Saúl!");
        alert.setContentText("Has completado el solitario con éxito.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}