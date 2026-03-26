package com.example.solitario.GUI;

import solitaire.TableauDeck;
import DeckOfCards.CartaInglesa;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class TableauDeckGrafico extends Pane {
    private ArrayList<CartaInglesa> cartasParaDibujar;
    private boolean resaltarUltima = false;

    public TableauDeckGrafico(ArrayList<CartaInglesa> cartas) {
        this.cartasParaDibujar = cartas;
        this.setMinWidth(100);
        this.setMinHeight(400);
        actualizar();
    }

    public void setResaltarUltima(boolean resaltar) {
        this.resaltarUltima = resaltar;
        actualizar();
    }

    public void actualizar() {
        this.getChildren().clear();
        if (cartasParaDibujar == null) return;

        for (int i = 0; i < cartasParaDibujar.size(); i++) {
            CartaGrafica visual = new CartaGrafica(cartasParaDibujar.get(i));
            visual.setLayoutY(i * 30);
            visual.setMouseTransparent(true);

            if (resaltarUltima && cartasParaDibujar.get(i).isFaceup()) {
                visual.setResaltado(true);
            }
            this.getChildren().add(visual);
        }
    }
}