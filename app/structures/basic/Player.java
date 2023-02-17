package structures.basic;

import game.logic.Gui;

import java.util.ArrayList;
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
	protected Deck deck;
	protected ArrayList<Unit> units; // store all units currently on board
	
	
	public Player() {
		super();
		setHealth(20);
		setMana(2);
		hand = new Card[6];
		deck = new Deck(1);
		this.units = new ArrayList<>();
	}
	
	public Player(int health, int mana) {
		super();
		setHealth(health);
		setMana(mana);
		hand = new Card[6];
		deck = new Deck(1);
		this.units = new ArrayList<>();
	}

	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
		Gui.displayHumanHP(this);
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
		Gui.displayHumanMana(this);
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

	public Deck getDeck() {
		return this.deck;
	}

	// draw a card from the deck and place it in the player's hand.
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

		// show changes on front-end
		hand[i] = current;
		cardsInHand++;
		Gui.displayCard(current, i + 1);
	}
	
	public ArrayList<Unit> getUnits(){
		return this.units;
	}
	
	public void setUnit(Unit unit) {
		this.units.add(unit);
	}

}
