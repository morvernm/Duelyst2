package structures.basic;

import org.checkerframework.checker.units.qual.A;
import utils.OrderedCardLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A basic representation of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Player {

	int health;
	int mana;
	protected Card[] hand;
	protected int cardsInHand = 0;
	protected ArrayList<Card> deck;
	protected ArrayList<Unit> units; // store all units currently on board
	
	public Player() {
		super();
		this.health = 20;
		this.mana = 0;
		hand = new Card[6];
		createDeck();
		drawFirst3();
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
		hand = new Card[6];
		createDeck();
		drawFirst3();
	}

	private void createDeck() {
		this.deck = new ArrayList<>(OrderedCardLoader.getPlayer1Cards()); // Get cards and load into ArrayList
	}

	private void drawFirst3(){ // in first round, load 3 cards into player's hand
		for(int i = 0; i < 3; i++) {
			drawCard();
		}
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	// get a card at a specified position in a player's hand
	public Card getCard(int position) {
		if(!(position >= 1 && position <= 6)) throw new IllegalArgumentException("Card position must be between 1 - 6");
		return hand[position - 1]; // use range 1 - 6 to reflect front-end display logic.
	}

	public void removeFromHand(int position) { // remove a card from the hand at a given position
		hand[position-1] = null; // Set position to null to remove card. Use range 1 - 6 to reflect front-end display logic.
		cardsInHand--;
	}

	// draw a card from the deck and place it in the player's hand.
	public void drawCard() {
		if (deckIsEmpty()) throw new NoSuchElementException("Deck is empty");
		if (cardsInHand == 6) { // if no space in hand, card is lost
			deck.remove(0);
			return;
		}
		// if is space in hand, find first free spot in hand and place card in it.
		int i = 0;
		while(hand[i] != null) {
			i++;
		}
		hand[i] = deck.remove(0);
		cardsInHand++;
	}

	public int getDeckSize() {
		return deck.size();
	}

	public boolean deckIsEmpty() {
		return deck.isEmpty();
	}
}
