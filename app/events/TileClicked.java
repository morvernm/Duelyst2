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
import structures.basic.Card;
import structures.basic.Playable;
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

		int status = gameState.getStatus();

		if (gameState.board[tilex][tiley].getOccupier() != null) {  // check if selected tile has a unit on it
			if (gameState.previousAction.isEmpty() && gameState.getHumanPlayer().getUnits().contains(gameState.board[tilex][tiley].getOccupier())) {
				Unit unit = gameState.board[tilex][tiley].getOccupier();
				
				gameState.validMoves = Utility.determineValidMoves(gameState.board, unit);
				Gui.highlightTiles(out, gameState.validMoves, 1);
				
				gameState.validAttacks = Utility.determineTargets(gameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()], gameState.validMoves, GameState.enemy, gameState.board);
				
				Gui.highlightTiles(out, gameState.validAttacks, 2);
				
				gameState.previousAction.push(unit);
				
				System.err.println("Added to the Stack " + gameState.previousAction.peek());
				
			} 
			else {
				if (gameState.previousAction.peek() instanceof Unit) {		
					//get unit from stack
					Unit unit = (Unit) GameState.getPreviousAction();
					Gui.removeHighlightTiles(out, GameState.board); //clearing board 
					// Determine if Adjacent or Distanced Attack aka. move and attack
					if (Utility.getValidTargets(GameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()], GameState.enemy, GameState.board).contains(gameState.board[tilex][tiley])) {
						
						
					} else if (gameState.validAttacks.contains(GameState.board[tilex][tiley])) {
						
						
					}
				}
			}		
		}
		
		// check if tile is free - can only move to an empty place 
		switch(status){
			/* Stack empty */
			case 0:
				break;
			/* Unit in stack */
			case 1:
				break;
			/* Unit card in stack */
			case 2:
				if (GameState.currentPlayer != GameState.humanPlayer){
					return;
				}
				Card card = (Card)GameState.previousAction.peek();
				Tile tile = GameState.board[tilex][tiley];
				Player player = GameState.currentPlayer;
				Player enemy = GameState.enemy;

				if (Utility.validMove(out, card, player, enemy, tile, GameState.board)){
					Gui.removeHighlightTiles(out, GameState.board);
					Utility.placeUnit(out, card, player, tile);
					GameState.emptyPreviousAction();
				}
				return;
			/* Spell card in stack */
			case 3:
				break;
		}

		if (gameState.board[tilex][tiley].getOccupier() == null && !gameState.previousAction.isEmpty()) {  
			
			if(gameState.validMoves.contains(gameState.board[tilex][tiley])) { // check if unit can move to selected tile
								
				Unit unit = (Unit) GameState.getPreviousAction(); //get unit from stack 
				
				System.out.println("Can move " + unit.hasMoved());
				
				gameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setOccupier(null); //clear unit from tile
				
				BasicCommands.moveUnitToTile(out, unit,gameState.board[tilex][tiley]); //move unit to chosen tiles
				unit.setPositionByTile(gameState.board[tilex][tiley]); //change position of unit to new tiles
				
				gameState.board[tilex][tiley].setOccupier(unit); //set unit as occupier of tiles
				
				
				Gui.removeHighlightTiles(out, gameState.board); //clearing board 
				
			}
//			need to do y movement too
		}

	}
	
	
	

	
}			


