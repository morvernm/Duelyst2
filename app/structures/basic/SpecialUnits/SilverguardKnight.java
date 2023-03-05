package structures.basic.SpecialUnits;

import game.logic.Gui;

public class SilverguardKnight extends Provoke {
	
	@SuppressWarnings("unused")
	private String name = "SilverguardKnight";
	
	/*
	 * 
	 * WIll need to add more stuff for the other ability
	 */
	public void buffAttack() {
		System.out.println("BUFFED");
		
		this.setAttack(this.getAttack() + 2);
		
		Gui.setUnitStats(this, this.getHealth(), this.getAttack());
	}
}
