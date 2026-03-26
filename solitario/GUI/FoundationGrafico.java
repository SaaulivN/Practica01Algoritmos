package com.example.solitario.GUI;

import solitaire.FoundationDeck;
import DeckOfCards.CartaInglesa;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FoundationGrafico extends StackPane {
    private FoundationDeck deckLogico;

    public FoundationGrafico(FoundationDeck deckLogico) {
        this.deckLogico = deckLogico;
        actualizar(); // Llamada inicial
    }

    public void actualizar() {
        this.getChildren().clear();

        Rectangle marco = new Rectangle(100, 145);
        marco.setFill(Color.web("#8B0000").deriveColor(0, 1, 0.8, 1));
        marco.setStroke(Color.WHITE);
        marco.setStrokeWidth(2);
        marco.setArcWidth(15);
        marco.setArcHeight(15);
        this.getChildren().add(marco);

        CartaInglesa ultima = deckLogico.getUltimaCarta();

        if (ultima != null) {
            ultima.makeFaceUp();
            CartaGrafica visual = new CartaGrafica(ultima);
            this.getChildren().add(visual);
        }
    }
}