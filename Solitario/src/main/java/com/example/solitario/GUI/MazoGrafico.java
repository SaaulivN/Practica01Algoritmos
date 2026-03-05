package com.example.solitario.GUI;

import DeckOfCards.CartaInglesa;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class MazoGrafico extends StackPane {
    private ArrayList<CartaInglesa> cartasLogicas;
    private ImageView vistaReverso;

    public MazoGrafico(ArrayList<CartaInglesa> cartas) {
        this.cartasLogicas = cartas;
        this.vistaReverso = new ImageView();

        try {
            Image imgReverso = new Image(getClass().getResourceAsStream("/com/example/solitario/Recursos/BackgroundCard.png"));
            vistaReverso.setImage(imgReverso);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el reverso en MazoGrafico");
        }

        vistaReverso.setFitWidth(100);
        vistaReverso.setPreserveRatio(true);

        this.getChildren().add(vistaReverso);
        actualizarMazo();
    }

    public void actualizarMazo() {
        if (cartasLogicas == null || cartasLogicas.isEmpty()) {
            this.setOpacity(0.3);
        } else {
            this.setOpacity(1.0);
        }
    }
}