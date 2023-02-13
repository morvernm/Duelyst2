package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import game.logic.Gui;
import structures.GameState;
import structures.basic.Card;

import java.util.HashMap;
import java.util.HashSet;

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
		highlightCard(handPosition, gameState);
	}


	public void highlightCard(int handPosition, GameState gameState) {
		clearHighlighted();
		// highlighted the selected card
		Card highlightedCard = gameState.getHumanPlayer().getCard(handPosition);
		Gui.highlightCard(highlightedCard, handPosition);
		currentlyHighlighted.put(highlightedCard, handPosition);
	}

	public static void clearHighlighted(){
		// Unhighlight currently highlighted cards
		if (!currentlyHighlighted.isEmpty()) {
			currentlyHighlighted.forEach((key, value) -> {
				Gui.displayCard(key, value);
			});
			currentlyHighlighted.clear();
		}
	}
}

