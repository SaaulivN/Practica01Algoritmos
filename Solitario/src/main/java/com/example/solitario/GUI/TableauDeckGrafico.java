package com.example.solitario.GUI;

import solitaire.TableauDeck;
import DeckOfCards.CartaInglesa;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class TableauDeckGrafico extends Pane {
    private TableauDeck deckLogico;
    private boolean resaltarUltima = false;

    public TableauDeckGrafico(TableauDeck deckLogico) {
        this.deckLogico = deckLogico;
        actualizar();
    }

    public void setResaltarUltima(boolean resaltar) {
        this.resaltarUltima = resaltar;
        actualizar();
    }

    public void actualizar() {
        this.getChildren().clear();

        Rectangle detector = new Rectangle(100, 500);
        detector.setFill(Color.TRANSPARENT);
        this.getChildren().add(detector);

        ArrayList<CartaInglesa> cartas = deckLogico.getCards();
        for (int i = 0; i < cartas.size(); i++) {
            CartaGrafica visual = new CartaGrafica(cartas.get(i));
            visual.setLayoutY(i * 30);
            visual.setMouseTransparent(true);

            if (resaltarUltima && cartas.get(i).isFaceup()) {
                visual.setResaltado(true);
            }

            this.getChildren().add(visual);
        }
    }
}