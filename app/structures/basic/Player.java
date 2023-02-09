package structures.basic;

import utils.OrderedCardLoader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

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
	protected ArrayDeque<Card> deck;
	protected ArrayList<Unit> units; // store all units currently on board
	
	public Player() {
		super();
		this.health = 20;
		this.mana = 0;
		createDeck();
		hand = new Card[6];
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
		createDeck();
		hand = new Card[6];
	}

	private void createDeck() {
		this.deck = new ArrayDeque<>(); // initialise deck;
		List<Card> player1Cards = OrderedCardLoader.getPlayer1Cards(); // Get cards
		for(int i = player1Cards.size() - 1; i >= 0; i--) {
			this.deck.push(player1Cards.get(i)); // put each card onto Player deque.
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
	public Card getHandCard(int position) { // get a card at a specified position in a player's hand
		return hand[position - 1]; // use range 1 - 6 to reflect front-end display logic.
	}

	public void removeFromHand(int position) { // remove a card from the hand at a given position
		if(hand[position - 1] == null) throw new IllegalArgumentException("No card exists at this hand position");
		this.hand[position - 1] = null;
	}

	public int drawCard() { // draw a card from the deck and place it in the player's hand. Return index card was placed in
		if(cardsInHand < 6) { // if space in hand
			int i = 0;
			while (i < 6) { // traverse until find free space in hand and place it there.
				if (hand[i] == null) {
					hand[i] = deck.pop();
					this.cardsInHand++;
					break;
				}
				i++;
			}
			return i + 1; // return position card was placed in (1-indexed to match front-end).
		}
		else throw new IllegalArgumentException("Cannot have more than 6 cards in hand");
	}

	public int getDeckSize() {
		return deck.size();
	}
}
