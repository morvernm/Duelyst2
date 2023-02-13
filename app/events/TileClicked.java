package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.BasicObjectBuilders;
import structures.Utility;

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
		
		int status = gameState.getStatus();

		switch (status){
			/* Stack empty */
			case 0:
				break;
			/* Unit in stack */
			case 1:
				break;
			/* Unit card in stack */
			case 2:
				Card card = gameState.peekStack();
				Tile[][] board = gameState.getBoard();
				Tile tile = board[tilex][tiley];
				Player player = gameState.getPlayer(true);
				Player enemy = gameState.getPlayer(false);

				Utility.validMove(card, player, enemy,tile, board);
				break;
			/* Spell card in stack */
			case 3:
				break;
		}
		
		BasicCommands.drawTile(out, gameState.board[tilex][tiley], 2);
		
	}

}
