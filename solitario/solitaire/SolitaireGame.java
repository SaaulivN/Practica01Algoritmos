package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import java.util.ArrayList;

public class SolitaireGame {
    ArrayList<TableauDeck> tableau = new ArrayList<>();
    ArrayList<FoundationDeck> foundation = new ArrayList<>();
    FoundationDeck lastFoundationUpdated;
    DrawPile drawPile;
    WastePile wastePile;
    private Pila<Movimiento> historial = new Pila<>(500);

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }

    public void reloadDrawPile() {
        ArrayList<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
    }

    public void drawCards() {
        ArrayList<CartaInglesa> cards = drawPile.retirarCartas();
        wastePile.addCartas(cards);
    }

    public void deshacer() {
        if (historial.pilaVacia()) return;

        Movimiento mov = historial.pop();
        switch (mov.tipo) {
            case WASTE_TO_TABLEAU -> {
                tableau.get(mov.destino).removerUltimaCarta();
                wastePile.addCartas(mov.cartas);
            }
            case TABLEAU_TO_TABLEAU -> {
                TableauDeck d = tableau.get(mov.destino);
                TableauDeck o = tableau.get(mov.origen);
                d.removeStartingAt(mov.cartas.get(0).getValor());

                if (mov.seDestapoCarta && !o.getCards().pilaVacia()) {
                    o.getCards().verTope().makeFaceDown();
                }
                o.agregarBloqueDeCartas(mov.cartas);
            }
            case WASTE_TO_FOUNDATION -> {
                CartaInglesa c = foundation.get(mov.destino).removerUltimaCarta();
                if (c != null) wastePile.addCartas(mov.cartas);
            }
            case TABLEAU_TO_FOUNDATION -> {
                CartaInglesa cartaAs = foundation.get(mov.destino).removerUltimaCarta();
                if (cartaAs != null) {
                    TableauDeck origenTableau = tableau.get(mov.origen);

                    if (mov.seDestapoCarta && !origenTableau.isEmpty()) {
                        origenTableau.getUltimaCarta().makeFaceDown();
                    }

                    ArrayList<CartaInglesa> bloqueAs = new ArrayList<>();
                    bloqueAs.add(cartaAs);
                    origenTableau.agregarBloqueDeCartas(bloqueAs);
                }
            }
        }
    }

    public boolean moveWasteToTableau(int tDestino) {
        TableauDeck dest = tableau.get(tDestino - 1);
        CartaInglesa carta = wastePile.verCarta();
        if (dest.agregarCarta(carta)) {
            wastePile.getCarta();
            ArrayList<CartaInglesa> movidas = new ArrayList<>();
            movidas.add(carta);
            historial.push(new Movimiento(Movimiento.Tipo.WASTE_TO_TABLEAU, -1, tDestino - 1, movidas, false));
            return true;
        }
        return false;
    }

    public boolean moveTableauToFoundation(int numero) {
        TableauDeck fuente = tableau.get(numero - 1);
        if (fuente.isEmpty()) return false;

        CartaInglesa carta = fuente.getUltimaCarta();

        boolean seDestapoRealmente = false;
        ArrayList<CartaInglesa> cartasActuales = fuente.getCardsAsList();
        if (cartasActuales.size() > 1) {
            CartaInglesa cartaAbajo = cartasActuales.get(cartasActuales.size() - 2);
            if (!cartaAbajo.isFaceup()) {
                seDestapoRealmente = true;
            }
        }

        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation.get(cualFoundation);

        if (destino.agregarCarta(carta)) {
            fuente.removerUltimaCarta();
            ArrayList<CartaInglesa> lista = new ArrayList<>();
            lista.add(carta);
            historial.push(new Movimiento(Movimiento.Tipo.TABLEAU_TO_FOUNDATION, numero-1, cualFoundation, lista, seDestapoRealmente));
            return true;
        }
        return false;
    }

    public boolean moveTableauToTableau(int tOrigen, int tDestino) {
        TableauDeck origen = tableau.get(tOrigen - 1);
        TableauDeck destino = tableau.get(tDestino - 1);

        if (origen.isEmpty()) return false;

        int valorBuscado = destino.isEmpty() ? 13 : destino.getUltimaCarta().getValor() - 1;

        CartaInglesa cartaBase = origen.viewCardStartingAt(valorBuscado);

        if (cartaBase != null && destino.sePuedeAgregarCarta(cartaBase)) {

            boolean seDestapo = false;
            ArrayList<CartaInglesa> listaOrigen = origen.getCardsAsList();
            int indexBase = -1;
            for(int i=0; i<listaOrigen.size(); i++) {
                if(listaOrigen.get(i) == cartaBase) { indexBase = i; break; }
            }

            if (indexBase > 0 && !listaOrigen.get(indexBase - 1).isFaceup()) {
                seDestapo = true;
            }

            ArrayList<CartaInglesa> bloque = origen.removeStartingAt(valorBuscado);
            destino.agregarBloqueDeCartas(bloque);

            historial.push(new Movimiento(
                    Movimiento.Tipo.TABLEAU_TO_TABLEAU,
                    tOrigen - 1,
                    tDestino - 1,
                    bloque,
                    seDestapo
            ));
            return true;
        }
        return false;
    }

    public boolean moveWasteToFoundation() {
        CartaInglesa carta = wastePile.verCarta();
        if (moveCartaToFoundation(carta)) {
            wastePile.getCarta();
            ArrayList<CartaInglesa> lista = new ArrayList<>();
            lista.add(carta);
            historial.push(new Movimiento(Movimiento.Tipo.WASTE_TO_FOUNDATION, -1, carta.getPalo().ordinal(), lista, false));
            return true;
        }
        return false;
    }

    private boolean moveCartaToFoundation(CartaInglesa carta) {
        if (carta == null) return false;
        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation.get(cualFoundation);
        lastFoundationUpdated = destino;
        return destino.agregarCarta(carta);
    }

    public boolean isGameOver() {
        for (FoundationDeck f : foundation) {
            if (f.estaVacio() || f.getUltimaCarta().getValor() != 13) return false;
        }
        return true;
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) foundation.add(new FoundationDeck(palo));
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck td = new TableauDeck();
            td.inicializar(drawPile.getCartas(i + 1));
            tableau.add(td);
        }
    }

    public DrawPile getDrawPile() { return drawPile; }
    public ArrayList<TableauDeck> getTableau() { return tableau; }
    public WastePile getWastePile() { return wastePile; }
    public ArrayList<FoundationDeck> getFoundations() { return foundation; }
}