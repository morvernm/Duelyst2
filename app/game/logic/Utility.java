package game.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import commands.BasicCommands;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;


public class Utility {
	/*
	 * This class is the utility class where methods with some main logic of the game will be provided
	 * 
	 */
	
	public static Set<Tile> determineTargets(Tile tile, ArrayList<Tile> positions, Player enemy, Tile[][] board){
		
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
	
	
	
}
