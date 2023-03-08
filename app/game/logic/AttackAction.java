package game.logic;

import structures.basic.Tile;
import structures.basic.Unit;

public class AttackAction {
	
	public Unit unit;
	public Tile tile;
	public Integer value;
	
	public AttackAction(Unit unit, Tile tile) {
		// TODO Auto-generated constructor stub
		this.unit = unit;
		this.tile = tile;
		this.value = -1;
	}
	public AttackAction() { //to allow for overriding constructor in child classes. 
		
	}

}
