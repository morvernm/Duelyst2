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
	public static ArrayList<Tile> getValidTargets(Tile tile, Player enemy, Tile[][] board){
		
		ArrayList<Tile> validAttacks = new ArrayList<>();
		
		for (Unit unit : enemy.getUnits()) {
			System.out.println("Position of enemy is: " + unit.getPosition().getTilex() + " " + unit.getPosition().getTiley());
			int unitx = unit.getPosition().getTilex();
			int unity = unit.getPosition().getTiley();
			
						
			if (unitx - tile.getTilex() < 2 && unity - tile.getTiley() < 2 ) {
				validAttacks.add(board[unitx][unity]);
			}
		}
		for (Tile t : validAttacks) {
			System.out.println("Enemy on " + t.getTilex() + " " + t.getTiley());
		}
		return validAttacks;
		
	}
	
	
	
}
