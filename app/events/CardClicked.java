package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.Card;
import structures.basic.spellcards.SpellCard;
import structures.basic.spellcards.Sundrop;
import structures.basic.spellcards.Truestrike;

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
	private HashMap<String, SpellCard> spellcards = new HashMap<String,SpellCard>();
	private SpellCard currentSpellcard;
	private static int handPosition;

	public CardClicked() {
		initaliseSpellcards();
	}

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		this.handPosition = message.get("position").asInt();
		highlightCard(handPosition, gameState);
		Gui.removeHighlightTiles(out, GameState.getBoard());
		// ensure there aren't duplicate spellcards already in the previousActions waiting to be played
		if(GameState.previousAction.contains(currentSpellcard)) GameState.previousAction.remove(currentSpellcard);

		if(spellcards.get(gameState.getHumanPlayer().getCard(handPosition).getCardname()) != null) { // if currently selected card is a spellcard
			// Create an instance of the spellcard:
			currentSpellcard = spellcards.get(gameState.getHumanPlayer().getCard(handPosition).getCardname()) ;

			// Highlight tiles
			currentSpellcard.highlightTargets(out);

			GameState.setPreviousAction(currentSpellcard);

		}
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

	public void initaliseSpellcards(){
		spellcards.put("Truestrike", new Truestrike());
		spellcards.put("Sundrop Elixir", new Sundrop());
	}

	public static int getHandPosition() {
		return handPosition;
	}
}

