package structures.basic.SpecialUnits;

import akka.actor.ActorRef;
import commands.BasicCommands;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.Unit;

public class AzuriteLion extends Unit{
	private final String name = "AzuriteLion";
	private int attackCount = 0;
	private boolean attacked;

	public int getAttackCount() {
		return attackCount;
	}
	public void setAttacked() {
		attacked = false;
		attackCount+=1;
	}
	public boolean hasAttacked() {
		if(attackCount == 2) {
			attacked = true;
		}
		return this.attacked;
	}

	

}
