package game.logic;

import java.util.ArrayList;

import commands.BasicCommands;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;


public class Utility {
	/*
	 * This class is the utility class where methods with some main logic of the game will be provided
	 * 
	 */
	
	public static ArrayList<Tile> determineTargets(Tile tile, ArrayList<Tile> positions, Player enemy, Tile[][] board){
		
		ArrayList<Tile> validAttacks = new ArrayList<>();
		
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
	
	public static ArrayList<Tile> getValidTargets(Tile tile, Player enemy, Tile[][] board){
		
		ArrayList<Tile> validAttacks = new ArrayList<>();
		
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
