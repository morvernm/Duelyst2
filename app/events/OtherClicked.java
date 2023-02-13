package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import game.logic.Gui;
import structures.GameState;
import structures.basic.Card;

import java.util.HashMap;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * { 
 *   messageType = “otherClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class OtherClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		CardClicked.clearHighlighted();
		
	}

}


