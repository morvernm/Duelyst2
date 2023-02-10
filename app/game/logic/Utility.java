package game.logic;

import java.util.ArrayList;

import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;


public class Utility {
	/*
	 * This class is the utility class where methods with some main logic of the game will be provided
	 * 
	 */
	public static ArrayList<Tile> getValidATargets(Tile tile, Player enemy, Tile[][] board){
		
		ArrayList<Tile> validAttacks = new ArrayList<>();
		
		for (Unit unit : enemy.getUnits()) {
			int unitx = unit.getPosition().getTilex();
			int unity = unit.getPosition().getTiley();
			
			if (unitx - tile.getTilex() == 1 && unity - tile.getTiley() == 1) {
				validAttacks.add(board[unitx][unity]);
			}
		}
		return validAttacks;
		
	}
	
	
	
}
