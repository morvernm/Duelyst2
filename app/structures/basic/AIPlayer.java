package structures.basic;

import game.logic.Gui;

import java.util.NoSuchElementException;

public class AIPlayer extends Player{

    public AIPlayer() {
        super(20, 0, 2);
        // Draw first three cards
        for(int i = 0; i < 3; i++) {
            drawCard();
        }
    }

    @Override
    public void setMana(int mana){
        this.mana = Math.min(mana, 9); // cap the max amount of mana a player can have at 9, as per GUI.
        Gui.displayAIMana(this);
    }
    @Override
    public void setHealth(int health){
        this.health = health;
        Gui.displayAIHP(this);
    }
    @Override
    public void drawCard() {
        if (deck.isEmpty()) throw new NoSuchElementException("Deck is empty");
        if (cardsInHand == 6) { // if no space in hand, card is lost
            deck.drawTopCard();
            return;
        }
        // if is space in hand, find first free spot in hand and place card from deck in it.
        int i = 0;
        while(hand[i] != null && i < hand.length - 1) {
            i++;
        }
        Card current = deck.drawTopCard();

        hand[i] = current;
        cardsInHand++;
    }
}
