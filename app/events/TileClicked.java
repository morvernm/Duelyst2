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
		
		// if there is a unit on the tile and the unit is not an enemy unit
		if (gameState.board[tilex][tiley].getOccupier() != null) {  // check if selected tile has a unit on it
			
			if (gameState.previousAction.isEmpty()) {
				
				if (!gameState.enemy.getUnits().contains(gameState.board[tilex][tiley].getOccupier())) {
					
					Unit unit = gameState.board[tilex][tiley].getOccupier();
					
					if (!unit.hasMoved() && !unit.hasAttacked()) {
						gameState.validMoves = Utility.determineValidMoves(gameState.board, unit);
						Gui.highlightTiles(out, gameState.validMoves, 1);
						gameState.validAttacks = Utility.determineTargets(gameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()], gameState.validMoves, GameState.enemy, gameState.board);
						Gui.highlightTiles(out, gameState.validAttacks, 2);
						gameState.previousAction.push(unit);
						
					} else if (unit.hasMoved() && !unit.hasAttacked()) {
						gameState.validAttacks = Utility.getValidTargets(gameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()], gameState.enemy, gameState.board);
						Gui.highlightTiles(out, gameState.validAttacks, 2);
						gameState.previousAction.push(unit);
						
					} else {
						BasicCommands.addPlayer1Notification(out, "Unit can no longer move or attack", 5);
					}
				} else {
					
					BasicCommands.addPlayer1Notification(out, "Cannot select enemy units", 5);
				}
				
			} else {
				
				if (gameState.previousAction.peek() instanceof Unit) {

					//get unit from stack
					Unit unit = (Unit) GameState.getPreviousAction();
					Gui.removeHighlightTiles(out, GameState.board); //clearing board 
					
					// Determine if Adjacent or Distanced Attack aka. move and attack
					if (Utility.getValidTargets(GameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()], GameState.enemy, GameState.board).contains(gameState.board[tilex][tiley])) {
						Utility.adjacentAttack(unit, GameState.board[tilex][tiley].getOccupier());
						
					} else if (gameState.validAttacks.contains(GameState.board[tilex][tiley])) {
						Utility.distancedAttack();
					} 
					
				}

			}
			
		
			
		}
		
		// check if tile is free - can only move to an empty place 
		if (gameState.board[tilex][tiley].getOccupier() == null && !gameState.previousAction.isEmpty()) {  
			
			if(gameState.validMoves.contains(gameState.board[tilex][tiley])) { // check if unit can move to selected tile
								
				Unit unit = (Unit) GameState.getPreviousAction(); //get unit from stack 
				
									
				gameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setOccupier(null); //clear unit from tile
				
				BasicCommands.moveUnitToTile(out, unit,gameState.board[tilex][tiley]); //move unit to chosen tiles
				unit.setPositionByTile(gameState.board[tilex][tiley]); //change position of unit to new tiles
				
				gameState.board[tilex][tiley].setOccupier(unit); //set unit as occupier of tiles
				
				unit.setMoved();
				Gui.removeHighlightTiles(out, gameState.board); //clearing board 
				
				
			}
//			need to do y movement too
		}

	}
	
	
	

	
}			


