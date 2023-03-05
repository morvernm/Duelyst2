package structures.basic.SpecialUnits;

import structures.basic.Unit; 
/*
 * This class represents the units that can attack twice per turn 
 */
public abstract class AttackTwice extends Unit {
	private int attackCount = 0;
	private boolean attacked;
	
	public int getAttackCount() {
		return attackCount;
	}
	
//	overriding Unit methods
	
	public void setAttacked() {
		attacked = false; 
		attackCount+=1; //increase attack count after each attack
	}
	public boolean hasAttacked() {
		if(attackCount == 2) { //once unit has attacked twice, set attacked to true
			attacked = true;
		}
		return this.attacked;
	}
	public void clearAttacked() {
		this.attacked = false;
		this.attackCount = 0;
	}

}
