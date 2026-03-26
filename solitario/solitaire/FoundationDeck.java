package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;


public class FoundationDeck {
    private Palo palo;
    private Pila<CartaInglesa> cartas = new Pila<>(13);

    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.pilaVacia()) {
                if (carta.getValorBajo() == 1) {
                    cartas.push(carta);
                    return true;
                }
            } else {
                CartaInglesa ultima = cartas.verTope();
                if (ultima.getValorBajo() + 1 == carta.getValorBajo()) {
                    cartas.push(carta);
                    return true;
                }
            }
        }
        return false;
    }

    public CartaInglesa removerUltimaCarta() {
        return cartas.pop();
    }

    public boolean estaVacio() {
        return cartas.pilaVacia();
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.verTope();
    }
}