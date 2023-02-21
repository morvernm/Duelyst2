package structures.basic.SpecialUnits;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;

public abstract class Provoke extends Unit{
	
	private String name = null;
	private Set<Tile> validTiles;
	
	public Set<Tile> specialAbility(Unit unit){
		
		/*
		 * How many provoke units does the enemy has on the boards
		 */
		Set<Unit> provokeUnits = new HashSet<>();
		
		validTiles = new HashSet<>();
		
	
		validTiles.add(GameState.getBoard()[this.getPosition().getTilex()][this.getPosition().getTiley()]);

		unit.setMoved();

		return validTiles;
	}
	
	
	
	
}
