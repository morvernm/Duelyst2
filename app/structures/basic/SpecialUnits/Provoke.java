package structures.basic.SpecialUnits;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;

public abstract class Provoke extends Unit{
	
	private String name = null;
	private Set<Tile> validAttacks;
	
	public Set<Tile> attractAttack(Tile tile){
		System.out.println("Attack Attracted");
		/*
		 * How many provoke units does the enemy has on the boards
		 */
		validAttacks = new HashSet<>();	
		validAttacks.add(GameState.board[this.getPosition().getTilex()][this.getPosition().getTiley()]);

		return validAttacks;
	}
	
	public void disableUnit(Unit other) {
		
		System.out.println("Unit disabled");
		other.setMoved();
	
	}
}