package structures.basic;

import game.logic.Gui;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import akka.actor.ActorRef;
import commands.BasicCommands;

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
	public void removeUnit(Unit unit) {
		this.units.remove(unit);
	}
	
	public void createAvatar(ActorRef out) {
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 100, Unit.class);
		unit.setPositionByTile(GameState.getBoard()[3][2]); 
		GameState.getBoard()[3][2].setOccupier(unit);
		BasicCommands.drawUnit(out, unit, GameState.getBoard()[3][2]);
		GameState.getHumanPlayer().setUnit(unit);
		GameState.modifiyTotalUnits(1);
		Gui.setUnitStats(unit, GameState.getHumanPlayer().getHealth(), 2);
		unit.setHealth(GameState.getHumanPlayer().getHealth());
		unit.setAttack(2);
	}

}
