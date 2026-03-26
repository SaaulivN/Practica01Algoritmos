package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

public class TableauDeck {
    private Pila<CartaInglesa> cartas = new Pila<>(52);

    public void inicializar(ArrayList<CartaInglesa> cartasRecibidas) {
        for (CartaInglesa c : cartasRecibidas) cartas.push(c);
        if (!cartas.pilaVacia()) cartas.verTope().makeFaceUp();
    }

    public ArrayList<CartaInglesa> removeStartingAt(int value) {
        ArrayList<CartaInglesa> removed = new ArrayList<>();
        Pila<CartaInglesa> aux = new Pila<>(52);
        while (!cartas.pilaVacia() && cartas.verTope().isFaceup()) {
            CartaInglesa c = cartas.pop();
            aux.push(c);
            if (c.getValor() == value) break;
        }
        while (!aux.pilaVacia()) removed.add(aux.pop());
        if (!cartas.pilaVacia()) cartas.verTope().makeFaceUp();
        return removed;
    }

    public CartaInglesa viewCardStartingAt(int value) {
        Pila<CartaInglesa> aux = new Pila<>(52);
        CartaInglesa encontrada = null;
        while (!cartas.pilaVacia() && cartas.verTope().isFaceup()) {
            CartaInglesa c = cartas.pop();
            aux.push(c);
            if (c.getValor() == value) { encontrada = c; break; }
        }
        while (!aux.pilaVacia()) cartas.push(aux.pop());
        return encontrada;
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (sePuedeAgregarCarta(carta)) {
            carta.makeFaceUp();
            cartas.push(carta);
            return true;
        }
        return false;
    }

    public boolean agregarBloqueDeCartas(ArrayList<CartaInglesa> bloque) {
        if (bloque == null) return false;
        for (CartaInglesa c : bloque) {
            c.makeFaceUp(); // Forzar visibilidad al restaurar o mover
            cartas.push(c);
        }
        return true;
    }

    public boolean sePuedeAgregarCarta(CartaInglesa cartaPrueba) {
        if (cartas.pilaVacia()) {
            return cartaPrueba.getValor() == 13;
        }

        CartaInglesa tope = cartas.verTope();
        return (!tope.getColor().equals(cartaPrueba.getColor()) &&
                tope.getValor() == cartaPrueba.getValor() + 1);
    }

    public CartaInglesa removerUltimaCarta() {
        if (cartas.pilaVacia()) return null;
        CartaInglesa c = cartas.pop();
        if (!cartas.pilaVacia()) cartas.verTope().makeFaceUp();
        return c;
    }

    public Pila<CartaInglesa> getCards() { return cartas; }
    public boolean isEmpty() { return cartas.pilaVacia(); }
    public CartaInglesa getUltimaCarta() { return cartas.verTope(); }

    public ArrayList<CartaInglesa> getCardsAsList() {
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