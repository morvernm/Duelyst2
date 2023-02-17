package structures.basic;

import game.logic.Gui;

public class AIPlayer extends Player{

    public AIPlayer() {
        super(20, 0);
    }

    public void setMana(int mana){
        this.mana = mana;
        Gui.displayAIMana(this);
    }
}
