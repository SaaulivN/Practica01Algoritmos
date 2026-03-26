package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

public class WastePile {
    private Pila<CartaInglesa> cartas = new Pila<>(52);

    public void addCartas(ArrayList<CartaInglesa> nuevas) {
        for (CartaInglesa c : nuevas) {
            cartas.push(c);
        }
    }

    public ArrayList<CartaInglesa> emptyPile() {
        ArrayList<CartaInglesa> lista = new ArrayList<>();
        while (!cartas.pilaVacia()) {
            lista.add(cartas.pop());
        }
        return lista;
    }

    public CartaInglesa getCarta() { return cartas.pop(); }
    public CartaInglesa verCarta() { return cartas.verTope(); }
    public boolean hayCartas() { return !cartas.pilaVacia(); }

    public ArrayList<CartaInglesa> getCartas() {
        ArrayList<CartaInglesa> lista = new ArrayList<>();
        Pila<CartaInglesa> aux = new Pila<>(52);
        while (!cartas.pilaVacia()) aux.push(cartas.pop());
        while (!aux.pilaVacia()) {
            CartaInglesa c = aux.pop();
            lista.add(c);
            cartas.push(c);
        }
        return lista;
    }
}