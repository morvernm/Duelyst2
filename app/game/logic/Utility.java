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

	// Get positions of potential targets of a spell.
	public static Set<Tile> getSpellTargetPositions(ArrayList<Unit> targets) {
		HashSet<Tile> positions = new HashSet<>();

		for (Unit unit : targets) {
			int unitx = unit.getPosition().getTilex();
			int unity = unit.getPosition().getTiley();
			positions.add(GameState.getBoard()[unitx][unity]);
		}
		return positions;
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
