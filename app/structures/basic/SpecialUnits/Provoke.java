package structures.basic.SpecialUnits;

import java.util.Set;

import structures.basic.Tile;
import structures.basic.Unit;

public abstract class Provoke extends Unit{
	
	private String name = null;
	private Set<Tile> validTiles;
	
	public Set<Tile> specialAbility(){
		
		
		return this.validTiles;
	}
	
	
}
