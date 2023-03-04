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

	protected int health;
	protected int mana;
	protected Card[] hand;
	public int cardsInHand = 0;
	protected Deck deck;
	protected ArrayList<Unit> units; // store all units currently on board
	protected Unit avatar;
	
	
	public Player() {
		super();
		setHealth(15);
		setMana(2);
		hand = new Card[6];
		deck = new Deck(1);
		this.units = new ArrayList<>();
	}
	
	public Player(int health, int mana, int deckNum) {
		super();
		setHealth(health);
		setMana(mana);
		hand = new Card[6];
		deck = new Deck(deckNum);
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
		this.mana = Math.min(mana, 9); // cap the max amount of mana a player can have at 9, as per GUI.
		Gui.displayHumanMana(this);
	}
	public void updateMana(int mana){
		setMana(this.mana + mana);
	}

	// get a card at a specified position in a player's hand
	public Card getCard(int position) {
		if(!(position >= 1 && position <= 6)) throw new IllegalArgumentException("Card position must be between 1 - 6");
		return hand[position - 1]; // use range 1 - 6 to reflect front-end display logic.
	}

	public void removeFromHand(int position) { // remove a card from the hand at a given position
		hand[position-1] = null; // Set position to null to remove card. Use range 1 - 6 to reflect front-end display logic.
		cardsInHand--;
		Gui.deleteCard(position);
	}

	public Deck getDeck() {
		return this.deck;
	}

	public void testcard(Card card){
		int i = 0;
		while(hand[i] != null && i < hand.length - 1) {
			i++;
		}

		hand[i] = card;
		cardsInHand++;
		Gui.displayCard(card, i + 1);
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


	
//	public Unit getUnitByIndex(int index) {
//		return units.get(index);
//	}
	
	public void setUnit(Unit unit) {
		this.units.add(unit);
	}
	public void removeUnit(Unit unit) {
		this.units.remove(unit);
	}
	
	public void createAvatar(Unit unit) {
		this.avatar = unit;
	}

	public Unit getAvatar() {
		return avatar;
	}




}
