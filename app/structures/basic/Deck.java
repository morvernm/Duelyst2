package structures.basic;

import structures.basic.Card;
import utils.OrderedCardLoader;

import java.util.ArrayList;

public class Deck {
    protected ArrayList<Card> deck;

    public Deck(int playerNo) { // Takes player number as argument - 1 for human, 2 for AI. Creates appropriate deck.
        if(playerNo == 1) this.deck = new ArrayList<>(OrderedCardLoader.getPlayer1Cards()); // Get cards and load into ArrayList
        else this.deck = new ArrayList<>(OrderedCardLoader.getPlayer2Cards());
    }

    public int getDeckSize() {
        return deck.size();
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public Card drawTopCard() {
        return deck.remove(0);
    }


}
