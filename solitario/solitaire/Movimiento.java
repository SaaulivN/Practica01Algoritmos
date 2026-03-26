package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

public class Movimiento {
    public enum Tipo { WASTE_TO_TABLEAU, TABLEAU_TO_TABLEAU, WASTE_TO_FOUNDATION, TABLEAU_TO_FOUNDATION }

    public Tipo tipo;
    public int origen;
    public int destino;
    public ArrayList<CartaInglesa> cartas;
    public boolean seDestapoCarta;

    public Movimiento(Tipo tipo, int origen, int destino, ArrayList<CartaInglesa> cartas, boolean seDestapo) {
        this.tipo = tipo;
        this.origen = origen;
        this.destino = destino;
        this.cartas = new ArrayList<>(cartas);
        this.seDestapoCarta = seDestapo;
    }
}