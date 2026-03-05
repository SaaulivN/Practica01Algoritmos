package com.example.solitario;

import solitaire.*;
import com.example.solitario.GUI.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SolitarioApp extends Application {
    private SolitaireGame juego;
    private Pane root;

    private enum TipoSeleccion { NINGUNA, WASTE, TABLEAU }
    private TipoSeleccion seleccionActual = TipoSeleccion.NINGUNA;
    private int indiceSeleccionado = -1;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        juego = new SolitaireGame();
        renderizarTablero();

        Scene escena = new Scene(root, 1100, 720, Color.DARKRED);
        primaryStage.setTitle("Práctica 1 - solitario");
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    public void renderizarTablero() {
        root.getChildren().clear();

        // Sección del drawpile
        // ----------------------------------------------------------------------------------------
        MazoGrafico drawVisual = new MazoGrafico(juego.getDrawPile().getCartas());
        drawVisual.setLayoutX(50); drawVisual.setLayoutY(50);
        drawVisual.setOnMouseClicked(e -> {
            cancelarSeleccion();
            if (juego.getDrawPile().hayCartas()) juego.drawCards();
            else juego.reloadDrawPile();
            renderizarTablero();
        });
        root.getChildren().add(drawVisual);
        // -----------------------------------------------------------------------------------------

        // Sección del wastepile
        // -----------------------------------------------------------------------------------------
        if (!juego.getWastePile().getCartas().isEmpty()) {
            CartaGrafica wasteVisual = new CartaGrafica(juego.getWastePile().verCarta());
            wasteVisual.setLayoutX(175); wasteVisual.setLayoutY(50);
            wasteVisual.setResaltado(seleccionActual == TipoSeleccion.WASTE);
            wasteVisual.setOnMouseClicked(e -> manejarClicWaste());
            root.getChildren().add(wasteVisual);
        }
        // -------------------------------------------------------------------------------------------

        // Sección del foundation
        // ----------------------------------------------------------------------------------------
        for (int i = 0; i < 4; i++) {
            FoundationGrafico f = new FoundationGrafico(juego.getFoundations().get(i));
            f.setLayoutX(450 + (i * 130)); f.setLayoutY(50);
            f.setOnMouseClicked(e -> manejarClicFoundation());
            root.getChildren().add(f);
        }
        // -----------------------------------------------------------------------------------------

        // Sección del tableau
        // ----------------------------------------------------------------------------------------
        for (int i = 0; i < 7; i++) {
            int numTableau = i + 1;
            TableauDeckGrafico t = new TableauDeckGrafico(juego.getTableau().get(i));
            t.setLayoutX(50 + (i * 140)); t.setLayoutY(250);

            if (seleccionActual == TipoSeleccion.TABLEAU && indiceSeleccionado == numTableau) {
                t.setResaltarUltima(true);
            }

            t.setOnMouseClicked(e -> manejarClicTableau(numTableau));
            root.getChildren().add(t);
        }
        // ----------------------------------------------------------------------------------------
    }

    // MANEJADORES DE LÓGICA DE CLIC

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
            // Intentar Movimiento a Tableau
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
            if (seleccionActual == TipoSeleccion.WASTE) juego.moveWasteToFoundation();
            else juego.moveTableauToFoundation(indiceSeleccionado);
            cancelarSeleccion();
        }
        renderizarTablero();
    }

    private void cancelarSeleccion() {
        seleccionActual = TipoSeleccion.NINGUNA;
        indiceSeleccionado = -1;
    }
}