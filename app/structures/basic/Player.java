package structures.basic;

import java.util.ArrayList;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Player {

	int health;
	int mana;
	
	protected ArrayList<Unit> units; // store all units currently on board

	
	public Player() {
		super();
		this.health = 20;
		this.mana = 0;
		this.units = new ArrayList<>();
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	
	public ArrayList<Unit> getUnits(){
		return this.units;
	}
	
	public void setUnit(Unit unit) {
		this.units.add(unit);
	}
	
	
	
}
