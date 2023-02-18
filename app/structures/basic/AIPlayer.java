package structures.basic;

import game.logic.Gui;

public class AIPlayer extends Player{

    public AIPlayer() {
        super(20, 0);
    }

    public void setMana(int mana){
        this.mana = Math.min(mana, 9); // cap the max amount of mana a player can have at 9, as per GUI.
        Gui.displayAIMana(this);
    }

    public void setHealth(int health){
        this.health = health;
        Gui.displayAIHP(this);
    }
}
