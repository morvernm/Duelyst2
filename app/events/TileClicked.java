package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		if (gameState.board[tilex][tiley].getOccupier() != null) {  // check if selected tile has a unit on it
				Unit occupier = gameState.board[tilex][tiley].getOccupier(); //	get the selected unit
//				check stack for previous move somehow. 
//				if(GameState.getPreviousAction() instanceof TileClicked) {
//					occupier.getPosition();
//				if no previous selection
					Utility.determineMove(out,GameState.getPlayer(),occupier,tilex,tiley);	
//					add this action to stack - not confident that this is how we would do it. 
					GameState.playerAction.push(this);
				}
		else {
			System.out.println("");
			}

		Gui.highlightTiles(out, Utility.showValidMoves(gameState.board, gameState.board[tilex][tiley].getOccupier()), 1);
		Gui.removeHighlightTiles(out, gameState.board);

		BasicCommands.drawTile(out,gameState.board[tilex][tiley],2);
		}
}
			


