package events;


import java.util.Set;

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
		
//		if tile has a unit on it
		if (gameState.getBoard()[tilex][tiley].getOccupier() != null) {  
				Unit unit = gameState.getBoard()[tilex][tiley].getOccupier();
				Set<Tile> validMoves = Utility.determineValidMoves(gameState.getBoard(), unit); //get valid tiles the unit can move to
				Gui.highlightTiles(out, validMoves, 1); //highlight tiles
//				gameState.getBoard()[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setOccupier(null); //clear unit from tile
				gameState.setPreviousAction(unit);
				
//			}
//			need to do y movement too
		}else if(gameState.getBoard()[tilex][tiley].getOccupier() == null && gameState.getPreviousAction() instanceof Unit) { 
			Unit unit = (Unit) gameState.getPreviousAction(); //get unit from stack 
				BasicCommands.moveUnitToTile(out, unit,gameState.getBoard()[tilex][tiley]); //move unit to chosen tiles
				gameState.getBoard()[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setOccupier(null); //clear unit from previous position
				unit.setPositionByTile(gameState.getBoard()[tilex][tiley]); //change position of unit to new tiles
				gameState.getBoard()[tilex][tiley].setOccupier(unit); //set unit as occupier of tiles
				Gui.removeHighlightTiles(out, gameState.getBoard()); //clearing board 
				unit.setMoved();
//			}
		}
}
}			


