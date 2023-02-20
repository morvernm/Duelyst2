package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.Card;
import structures.basic.SpellCard;
import structures.basic.Player;
import structures.basic.Tile;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{
	private static HashMap<Card, Integer> currentlyHighlighted = new HashMap<>();

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		int handPosition = message.get("position").asInt();
		try{
			Card card = highlightCard(handPosition, gameState);
			checkCardType(out, card);
		}catch (Exception E){
			return;
		}
	}

	public void checkCardType(ActorRef out, Card card){
		if (!(card instanceof SpellCard)){
			checkValidity(out, card);
			return;
		}
	}

	public void checkValidity(ActorRef out, Card card){
		Player human = GameState.getHumanPlayer();
		Player current = GameState.getCurrentPlayer();
		if (human == current){
			/* Correct player, so return if manacost allows card to be used */
			human.setMana(3);
			if (human.getMana() >= card.getManacost()){
				Player enemy = GameState.enemy;
				Set<Tile> s = Utility.cardPlacements(card, human, enemy, GameState.board);
				Gui.highlightTiles(out, s, 1);
			}
		}
	}


	public Card highlightCard(int handPosition, GameState gameState) {
		clearHighlighted();
		// highlighted the selected card
		Card highlightedCard = gameState.getHumanPlayer().getCard(handPosition);
		highlightedCard.setPositionInHand(handPosition);
		pushToPreviousAction(highlightedCard);
		Gui.highlightCard(highlightedCard, handPosition);
		currentlyHighlighted.put(highlightedCard, handPosition);
		return highlightedCard;
	}

	public static void clearHighlighted(){
		// Unhighlight currently highlighted cards
		currentlyHighlighted.clear();
	}

	public static void pushToPreviousAction(Card card){
		GameState.setPreviousAction(card);
	}
}

