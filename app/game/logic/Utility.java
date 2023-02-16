package game.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;

import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;


public class Utility {
	/*
	 * This class is the utility class where methods with some main logic of the game will be provided
	 * 
	 */	
	private static ActorRef out;

	public Utility(ActorRef out) {
		Utility.out = out;
	}
	
	
	
	public static Set<Tile> determineTargets(Tile tile, Set<Tile> positions, Player enemy, Tile[][] board){
		
		// Using Set so that the Tile Objects do not repeat for the last condition
		Set<Tile> validAttacks = new HashSet<>();
		
		// Has Attacked already
		if (tile.getOccupier().hasAttacked()) {
			return null;
		// Has moved but has not attacked - consider only the current position
		} else if (tile.getOccupier().hasMoved() && !tile.getOccupier().hasAttacked()) {
			return getValidTargets(tile, enemy, board);
		
		// Has not moved nor attacked - consider all possible movements as well. 
		} else if (!tile.getOccupier().hasMoved() && !tile.getOccupier().hasAttacked()) {
			System.out.println("has NOT moved NOR attacked");
			
			for (Tile position : positions) {
				validAttacks.addAll(getValidTargets(position,enemy,board));
			}		
		}
		return validAttacks;
	}
	
	public static Set<Tile> getValidTargets(Tile tile, Player enemy, Tile[][] board){
		
		Set<Tile> validAttacks = new HashSet<>();
		
		for (Unit unit : enemy.getUnits()) {
			int unitx = unit.getPosition().getTilex();
			int unity = unit.getPosition().getTiley();
			
			if (Math.abs(unitx - tile.getTilex()) < 2 && Math.abs(unity- tile.getTiley()) < 2 ) {
				validAttacks.add(board[unitx][unity]);
			}
		}
		return validAttacks;
	}
	/*
	 * Attacking logic
	 */
	public static void adjacentAttack(Unit attacker, Unit defender) {
		
		if (!attacker.hasAttacked()) {
			Gui.performAttack(attacker);
			
			//attacker.setAttacked();
			
			int newHealth = defender.getHealth()-attacker.getAttack();
			defender.setHealth(newHealth);
			Gui.setUnitStats(defender, defender.getHealth(), defender.getAttack());
		} 
	}
	
	
	public static void distancedAttack(Unit attacker, Unit defender, Player enemy) {
		System.out.println("Distanced Attack Activated");
		if (!attacker.hasAttacked() && !attacker.hasMoved()) {
			
			// Get the valid tiles from which the unit can attack
			ArrayList<Tile> validTiles = getValidAttackTiles(defender);
			
			int minScore = Integer.MAX_VALUE;
			Tile closestTile = null;
			
			// Find the closest/optimal position to attack from by scoring each option
			for (Tile tile : validTiles) {
				int score = 0;
				score += Math.abs(tile.getTilex() - attacker.getPosition().getTilex());
				score += Math.abs(tile.getTiley() - attacker.getPosition().getTiley());
				if (score < minScore) {
					minScore = score;
					closestTile = tile;
				}
				
			}
			
			// move unit to the closest tile
			if (closestTile != null) {
				System.out.println("The closest tile is: x = " + closestTile.getTilex() + " and y = " + closestTile.getTiley() + " score " + minScore);
				moveUnit(attacker, closestTile);
				if (minScore < 2) {
					try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				} else {
					try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
				}
				adjacentAttack(attacker, defender);
			
			}
		}
	}
	
	
	/*
	 * Gets the valid attack positions for distanced attacks (move first and then attack)
	 */
	
	public static ArrayList<Tile> getValidAttackTiles(Unit unit) {
		ArrayList<Tile> validTiles = new ArrayList<>();
		
		for (Tile tile : GameState.validMoves) {
			int unitx = unit.getPosition().getTilex();
			int unity = unit.getPosition().getTiley();
			if (Math.abs(unitx - tile.getTilex()) < 2 && Math.abs(unity- tile.getTiley()) < 2 ) {
				validTiles.add(tile);
			}
				
		}
		
		for (Tile tile : validTiles) {
			System.out.println("tile: x = " + tile.getTilex() + " and y = " + tile.getTiley());
		}
		
		return validTiles;
	}
	
	
	
	public static void moveUnit(Unit unit, Tile tile) {
		GameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setOccupier(null); //clear unit from tile
		
		BasicCommands.moveUnitToTile(out, unit,tile); //move unit to chosen tiles
		unit.setPositionByTile(tile); //change position of unit to new tiles
		
		tile.setOccupier(unit); //set unit as occupier of tiles
		
		//unit.setMoved();
		Gui.removeHighlightTiles(out, GameState.board); //clearing board 
	}
	
	
	
	public static Set<Tile> determineValidMoves(Tile[][] board, Unit unit) {

		Set<Tile> validTiles = new HashSet<>();

		if (!unit.hasMoved() && !unit.hasAttacked()) {
			int x = unit.getPosition().getTilex();
			int y = unit.getPosition().getTiley();
			// check one behind
			int newX = x - 1;
			if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
				validTiles.add(board[newX][y]);
				// if one behind empty, check two behind
				newX = x - 2;
				if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
					validTiles.add(board[newX][y]);
				}
			}
			// check one ahead
			newX = x + 1;
			if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
				validTiles.add(board[newX][y]);
				// if one ahead empty, check two ahead
				newX = x + 2;
				if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
					validTiles.add(board[newX][y]);
				}
			}
			// check one up
			int newY = y - 1;
			if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
				validTiles.add(board[x][newY]);
				// if one up empty, check two up
				newY = y - 2;
				if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
					validTiles.add(board[x][newY]);
				}
			}
			// check one down
			newY = y + 1;
			if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
				validTiles.add(board[x][newY]);
				// if one up empty, check two up
				newY = y + 2;
				if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
					validTiles.add(board[x][newY]);
				}
			}

			// diagonal tiles
			if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1].getOccupier() == null) {
				validTiles.add(board[x + 1][y + 1]);
			}

			if (x - 1 >= 0 && y - 1 >= 0 && board[x - 1][y - 1].getOccupier() == null) {
				validTiles.add(board[x - 1][y - 1]);
			}

			if (x + 1 < board.length && y - 1 >= 0 && board[x + 1][y - 1].getOccupier() == null) {
				validTiles.add(board[x + 1][y - 1]);
			}

			if (x - 1 >= 0 && y + 1 < board[0].length && board[x - 1][y + 1].getOccupier() == null) {
				validTiles.add(board[x - 1][y + 1]);
			}

		} else {
			// cannot move, so what happens? just return empty set?
			return validTiles;
		}
		return validTiles;
	}	
	
	
}
